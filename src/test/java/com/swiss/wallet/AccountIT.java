package com.swiss.wallet;

import com.swiss.wallet.entity.Role;
import com.swiss.wallet.web.dto.AccountResponseDto;
import com.swiss.wallet.web.dto.ResponseGlobalDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AccountIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void getAccountCurrent_WithValidToken_ReturnAccountResponseDtoStatus200(){
        AccountResponseDto responseDto = testClient
                .get()
                .uri("/api/v3/accounts/current")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.id()).isEqualTo(400);
        org.assertj.core.api.Assertions.assertThat(responseDto.value()).isEqualTo(0);

        responseDto = testClient
                .get()
                .uri("/api/v3/accounts/current")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.id()).isEqualTo(500);
        org.assertj.core.api.Assertions.assertThat(responseDto.value()).isEqualTo(0);

    }

}
