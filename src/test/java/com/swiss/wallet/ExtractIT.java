package com.swiss.wallet;

import com.swiss.wallet.web.dto.AccountResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ExtractIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void findExtractByUserLogged_WithValidToken_ReturnStatus204(){
        testClient
                .get()
                .uri("/api/v3/extracts/current")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .exchange()
                .expectStatus().isNoContent();

        testClient
                .get()
                .uri("/api/v3/extracts/current")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange()
                .expectStatus().isNoContent();
    }

}
