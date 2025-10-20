package com.example.Gestao_Viva;

import com.example.Gestao_Viva.model.StatusParque;
import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.model.enums.StatusVisita;
import com.example.Gestao_Viva.repository.StatusParqueRepository;
import com.example.Gestao_Viva.repository.VisitaRepository;
import com.example.Gestao_Viva.service.EmailService;
import io.restassured.RestAssured;
import io.restassured.config.RedirectConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatusParqueAdminPostTest {

  @LocalServerPort
  int port;
  
  @Autowired
  StatusParqueRepository repo;

  @Autowired
  VisitaRepository visitaRepo;

  @MockBean
  private EmailService emailService;

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
      registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb_gestao_viva");
      registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
      registry.add("spring.datasource.username", () -> "sa");
      registry.add("spring.datasource.password", () -> "password");
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
    repo.deleteAll();
    visitaRepo.deleteAll();
  }

  @ParameterizedTest(name = "POST /admin/status -> {0} ({1})")
  @CsvSource({
    "ABERTO, Funcionamento normal(teste - automatizado)",
    "FECHADO, Chuva forte(teste - automatizado)",
    "MANUTENCAO, Obras na passarela(teste - automatizado)"
  })
  void deveRegistrarEstados(String estado, String motivo) {
    given()
      .contentType("application/x-www-form-urlencoded")
      .formParam("estado", estado)
      .formParam("motivo", motivo)
    .when()
      .post("/admin/status")
    .then()
      .statusCode(anyOf(is(302), is(303)))
      .header("Location", containsString("/admin/status?sucesso"));

    StatusParque ultimo = repo.findTopByOrderByUltimaAtualizacaoDesc().orElseThrow();
    assertEquals(estado, ultimo.getEstado().name(), "estado salvo diferente");
    assertEquals(motivo, ultimo.getMotivo(), "motivo salvo diferente");
    assertNotNull(ultimo.getUltimaAtualizacao(), "sem timestamp");
  }

  @Test
  void deveNotificarVisitantesQuandoParqueForFechado() {
      Visita visitaDeHoje = new Visita();
      visitaDeHoje.setEmailResponsavel("visitante.hoje@email.com");
      visitaDeHoje.setNomeResponsavel("Visitante de Hoje");
      visitaDeHoje.setDataVisita(LocalDate.now());
      visitaDeHoje.setStatus(StatusVisita.AGENDADO);
      visitaDeHoje.setNomeInstituicao("Instituicao Notificada");
      visitaDeHoje.setTipoInstituicao("OUTRO");
      visitaDeHoje.setTelefoneResponsavel("111111");
      visitaDeHoje.setHorarioChegada("15:00");
      visitaDeHoje.setNumeroVisitantes(2);
      visitaRepo.save(visitaDeHoje);
      
      String motivoFechamento = "Alerta de maré alta";

      given()
          .contentType("application/x-www-form-urlencoded")
          .formParam("estado", "FECHADO")
          .formParam("motivo", motivoFechamento)
      .when()
          .post("/admin/status");

      // --- VERIFICAÇÃO FINAL E ROBUSTA ---
      // Verificamos que o método foi chamado para o destinatário correto,
      // com um assunto que contém a frase chave, e com qualquer corpo de texto.
      verify(emailService, times(1)).enviarEmail(
          eq("visitante.hoje@email.com"), 
          contains("AVISO IMPORTANTE"), 
          anyString()
      );
  }
}