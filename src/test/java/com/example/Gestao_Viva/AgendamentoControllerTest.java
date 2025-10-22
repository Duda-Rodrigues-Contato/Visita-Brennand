package com.example.Gestao_Viva;

import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.repository.VisitaRepository;
import com.example.Gestao_Viva.service.EmailService;
import io.restassured.RestAssured;
import io.restassured.config.RedirectConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Agendamento Controller — Teste de formulário de visita")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AgendamentoControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    VisitaRepository visitaRepo;

    @MockBean
    private EmailService emailService;

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb_agendamento;CHARSET=UTF8");
        
        registry.add("spring.mail.host", () -> "mockhost"); 
        
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.sql.init.mode", () -> "never");
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.config = RestAssured.config()
                .redirect(RedirectConfig.redirectConfig().followRedirects(false));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        visitaRepo.deleteAll(); 
    }

    @Test
    @DisplayName("SUCESSO: Deve agendar uma nova visita em grupo com sucesso e persistir dados")
    void deveAgendarNovaVisitaEmGrupoComSucesso() {
        LocalDate dataVisita = LocalDate.now().plusDays(1);
        while (dataVisita.getDayOfWeek() == DayOfWeek.MONDAY) {
            dataVisita = dataVisita.plusDays(1);
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dataVisitaStr = dataVisita.format(formatter);

        given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("nomeInstituicao", "Colégio Teste Integrado")
            .formParam("cnpj", "99.999.999/0001-00")
            .formParam("tipoInstituicao", "ESCOLA_PRIVADA")
            .formParam("nomeResponsavel", "João Testador")
            .formParam("emailResponsavel", "joao.teste@email.com")
            .formParam("telefoneResponsavel", "81999998888")
            .formParam("origemVisitante", "Recife/PE")
            .formParam("dataVisita", dataVisitaStr)
            .formParam("horarioChegada", "10:30")
            .formParam("incluiPcd", "true")
            .formParam("membros[0]", "Aluno A")
            .formParam("membros[1]", "Aluno B")
            .formParam("numeroVisitantes", "3")
        .when()
            .post("/agendamento")
        .then()
            .statusCode(is(302)) 
            .header("Location", containsString("/agendamento/sucesso"));

        List<Visita> visitasSalvas = visitaRepo.findAll();
        
        assertFalse(visitasSalvas.isEmpty(), "A visita não foi salva no banco de dados.");
        assertEquals(1, visitasSalvas.size(), "Apenas uma visita deveria ter sido salva.");

        Visita visitaSalva = visitasSalvas.get(0);
        
        assertEquals("Colégio Teste Integrado", visitaSalva.getNomeInstituicao());
        assertEquals(dataVisita, visitaSalva.getDataVisita());
        assertEquals(3, visitaSalva.getNumeroVisitantes());
        assertTrue(visitaSalva.isIncluiPcd());
        assertEquals("AGENDADO", visitaSalva.getStatus().name());
        assertEquals(2, visitaSalva.getMembros().size(), "Deveria ter 2 membros na lista");
    }
    
    @Test
    @DisplayName("FALHA: Deve retornar erro de validação para agendamento em uma segunda-feira")
    void deveFalharSeAgendarEmUmaSegundaFeira() {
        LocalDate proximaSegunda = LocalDate.now();
        while (proximaSegunda.getDayOfWeek() != DayOfWeek.MONDAY) {
            proximaSegunda = proximaSegunda.plusDays(1);
        }
        String dataSegundaStr = proximaSegunda.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("nomeInstituicao", "Escola Invalida")
            .formParam("tipoInstituicao", "ESCOLA_PRIVADA")
            .formParam("nomeResponsavel", "Responsável Inválido")
            .formParam("emailResponsavel", "teste.invalido@email.com")
            .formParam("telefoneResponsavel", "81911112222")
            .formParam("origemVisitante", "Recife/PE")
            .formParam("horarioChegada", "10:30")
            .formParam("numeroVisitantes", "2")
            .formParam("dataVisita", dataSegundaStr) 
        .when()
            .post("/agendamento")
        .then()
            .statusCode(is(200)) 
            .body(containsString("Agendamentos não são permitidos às segundas-feiras."));

        List<Visita> visitasSalvas = visitaRepo.findAll();
        assertTrue(visitasSalvas.isEmpty(), "Nenhuma visita deveria ter sido salva após falha de validação.");
    }
}