package com.example.Gestao_Viva;

import com.example.Gestao_Viva.model.StatusParque;
import com.example.Gestao_Viva.repository.StatusParqueRepository;
import io.restassured.RestAssured;
import io.restassured.config.RedirectConfig;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.internal.NameAndValue;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Admin /status — abrir, fechar e manutenção")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatusParqueAdminPostTest {

    // Cores
    static final String RESET="\u001B[0m", GREEN="\u001B[32m", CYAN="\u001B[36m", DIM="\u001B[2m", RED="\u001B[31m";

    // Watcher visual
    static class PrettyWatcher implements TestWatcher, BeforeTestExecutionCallback {
        private final ThreadLocal<Long> start = new ThreadLocal<>();
        @Override public void beforeTestExecution(ExtensionContext c){ start.set(System.nanoTime()); }
        @Override public void testSuccessful(ExtensionContext c){
            long ms=(System.nanoTime()-start.get())/1_000_000;
            System.out.println(GREEN+"✔ SUCESSO"+RESET+"  "+CYAN+c.getDisplayName()+RESET+"  "+DIM+"("+ms+" ms)"+RESET);
        }
        @Override public void testFailed(ExtensionContext c, Throwable t){
            long ms=(System.nanoTime()-start.get())/1_000_000;
            System.out.println(RED+"✖ FALHA"+RESET+"  "+CYAN+c.getDisplayName()+RESET+"  "+DIM+"("+ms+" ms)"+RESET);
            System.out.println(RED+"↳ "+t.getMessage()+RESET);
        }
    }
    @RegisterExtension static PrettyWatcher prettyWatcher = new PrettyWatcher();

    // Log HTTP compacto
    static final Filter PRETTY_HTTP = new Filter(){
        @Override public Response filter(FilterableRequestSpecification req, FilterableResponseSpecification res, FilterContext ctx){
            long t0=System.nanoTime(); Response r=ctx.next(req,res); long ms=(System.nanoTime()-t0)/1_000_000;
            String form=req.getFormParams().entrySet().stream().map(e->e.getKey()+"="+e.getValue()).collect(Collectors.joining("&"));
            String headers=r.getHeaders().asList().stream().filter(h->h.getName().equalsIgnoreCase("Location")).map(NameAndValue::toString).collect(Collectors.joining(", "));
            System.out.println(DIM+String.format("HTTP %d  %s %s  body:{%s}  hdr:{%s}  [%d ms]",
                    r.getStatusCode(), req.getMethod(), req.getURI(), form, headers, ms)+RESET);
            return r;
        }
    };

    @LocalServerPort int port;

    @Autowired StatusParqueRepository repo;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r){
        r.add("spring.datasource.url", ()->"jdbc:h2:mem:testdb_gestao_viva");
        r.add("spring.datasource.driver-class-name", ()->"org.h2.Driver");
        r.add("spring.datasource.username", ()->"sa");
        r.add("spring.datasource.password", ()->"password");
        r.add("spring.jpa.hibernate.ddl-auto", ()->"create-drop");
        r.add("spring.sql.init.mode", ()->"never");
    }

    @BeforeEach
    void setup(TestInfo info){
        RestAssured.baseURI="http://localhost";
        RestAssured.port=port;
        RestAssured.config=RestAssured.config().redirect(RedirectConfig.redirectConfig().followRedirects(false));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        repo.deleteAll();
        System.out.println(DIM+"• Iniciando: "+info.getDisplayName()+RESET);
    }

    @ParameterizedTest(name="[{index}] POST /admin/status -> {0} ({1})")
    @CsvSource({
            "ABERTO, Funcionamento normal(teste - automatizado)",
            "FECHADO, Chuva forte(teste - automatizado)",
            "MANUTENCAO, Obras na passarela(teste - automatizado)"
    })
    void deveRegistrarEstados(String estado, String motivo){
        given()
            .filter(PRETTY_HTTP)
            .contentType("application/x-www-form-urlencoded")
            .formParam("estado", estado)
            .formParam("motivo", motivo)
        .when()
            .post("/admin/status")
        .then()
            .statusCode(anyOf(is(302), is(303)))
            .header("Location", containsString("/admin/status?sucesso"));

        StatusParque ultimo = repo.findTopByOrderByUltimaAtualizacaoDesc().orElseThrow();
        assertEquals(estado, ultimo.getEstado().name());
        assertEquals(motivo, ultimo.getMotivo());
        assertNotNull(ultimo.getUltimaAtualizacao());
    }
}
