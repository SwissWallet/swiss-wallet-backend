package com.swiss.wallet;

import com.swiss.wallet.service.AccountService;
import com.swiss.wallet.web.dto.ExtractResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ExtractIT {

    @Autowired
    WebTestClient testClient;

    @Autowired
    private AccountService accountService;

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


    @Test
    public void findExtractByUserLogged_WithValidToken_ReturnExtractResponseDtoStatus200(){
        accountService.registerDeposit("carlos@email.com", 100D);
        List<ExtractResponseDto> responseDto = testClient
                .get()
                .uri("/api/v3/extracts/current")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .exchange()
                .expectBodyList(ExtractResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.get(0).value()).isEqualTo(100);

    }
}
