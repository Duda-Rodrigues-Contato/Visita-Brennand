package com.example.Gestao_Viva;

import com.example.Gestao_Viva.model.StatusParque;
import com.example.Gestao_Viva.repository.StatusParqueRepository;
import io.restassured.RestAssured;
import io.restassured.config.RedirectConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StatusParqueAdminPostTest {

  @LocalServerPort int port;
  @Autowired StatusParqueRepository repo;

  @BeforeEach
  void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
    RestAssured.config = RestAssured.config()
      .redirect(RedirectConfig.redirectConfig().followRedirects(false));
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    repo.deleteAll();
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
}
