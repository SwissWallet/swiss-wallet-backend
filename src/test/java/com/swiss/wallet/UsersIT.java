package com.swiss.wallet;

import com.swiss.wallet.entity.Role;
import com.swiss.wallet.web.dto.AddressCreateDto;
import com.swiss.wallet.web.dto.UserAddressCreateDto;
import com.swiss.wallet.web.dto.UserCreateDto;
import com.swiss.wallet.web.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsersIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createUser_WithValidUsernameAndPassword_ReturnUserCreatedWithStatus201(){
        UserResponseDto responseDto = testClient
                .post()
                .uri("/api/v3/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(
                    new UserAddressCreateDto(
                            new UserCreateDto(
                                    "tobby@email.com",
                                            "123456",
                                            "Tobby Henrique",
                                            "89018551007",
                                            "20/05/2001",
                                            "11987654321"
                            ),
                            new AddressCreateDto(
                                    "01000-000",
                                    "Alameda Joaquina",
                                    "Taboão da Serra",
                                    18L,
                                    "SP"
                            )
                    )
                )
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.id()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.username()).isEqualTo("tobby@email.com");
        org.assertj.core.api.Assertions.assertThat(responseDto.role()).isEqualTo(Role.ROLE_CLIENT);
    }
}
