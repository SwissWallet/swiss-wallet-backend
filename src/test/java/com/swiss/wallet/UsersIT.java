package com.swiss.wallet;

import com.swiss.wallet.entity.Role;
import com.swiss.wallet.web.dto.*;
import com.swiss.wallet.web.exception.ErrorMessage;
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

    @Test
    public void createUser_WithInvalidUsername_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .post()
                .uri("/api/v3/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(
                        new UserAddressCreateDto(
                                new UserCreateDto(
                                        "",
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
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

        responseDto = testClient
                .post()
                .uri("/api/v3/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(
                        new UserAddressCreateDto(
                                new UserCreateDto(
                                        "tobby@email",
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
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

        responseDto = testClient
                .post()
                .uri("/api/v3/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(
                        new UserAddressCreateDto(
                                new UserCreateDto(
                                        "tobby@",
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
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUser_WithInvalidPassword_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .post()
                .uri("/api/v3/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(
                        new UserAddressCreateDto(
                                new UserCreateDto(
                                        "tobby@email.com",
                                        "",
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
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

        responseDto = testClient
                .post()
                .uri("/api/v3/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(
                        new UserAddressCreateDto(
                                new UserCreateDto(
                                        "tobby@email.com",
                                        "123",
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
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);


    }

    @Test
    public void createUser_WithInvalidName_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .post()
                .uri("/api/v3/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(
                        new UserAddressCreateDto(
                                new UserCreateDto(
                                        "tobby@email.com",
                                        "123456",
                                        "",
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
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUser_WithInvalidCPF_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
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
                                        "00000000000",
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
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

        responseDto = testClient
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
                                        "",
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
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);


    }

    @Test
    public void createUser_WithInvalidBirthDate_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
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
                                        "",
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
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUser_WithInvalidZipCode_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
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
                                        "20/01/2000",
                                        "11930152076"
                                ),
                                new AddressCreateDto(
                                        "",
                                        "Alameda Joaquina",
                                        "Taboão da Serra",
                                        18L,
                                        "SP"
                                )
                        )
                )
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUser_WithInvalidStreet_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
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
                                        "20/01/2000",
                                        "11930152076"
                                ),
                                new AddressCreateDto(
                                        "06766-135",
                                        "",
                                        "Taboão da Serra",
                                        18L,
                                        "SP"
                                )
                        )
                )
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUser_WithInvalidCity_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
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
                                        "20/01/2000",
                                        "11930152076"
                                ),
                                new AddressCreateDto(
                                        "06766-135",
                                        "Alameda Joaquina",
                                        "",
                                        18L,
                                        "SP"
                                )
                        )
                )
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUser_WithInvalidNumber_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
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
                                        "20/01/2000",
                                        "11930152076"
                                ),
                                new AddressCreateDto(
                                        "06766-135",
                                        "Alameda Joaquina",
                                        "São Paulo",
                                        null,
                                        "SP"
                                )
                        )
                )
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUser_WithInvalidUf_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
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
                                        "20/01/2000",
                                        "11930152076"
                                ),
                                new AddressCreateDto(
                                        "06766-135",
                                        "Alameda Joaquina",
                                        "",
                                        18L,
                                        ""
                                )
                        )
                )
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUser_WithInvalidPhone_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
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
                                        "20/01/2000",
                                        ""
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
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

        responseDto = testClient
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
                                        "20/01/2000",
                                        "11"
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
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

        responseDto = testClient
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
                                        "20/01/2000",
                                        "987654321"
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
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);


    }

    @Test
    public void createUser_WithDuplicateUsername_ReturnErrorMessageStatus409(){
        ErrorMessage responseDto = testClient
                .post()
                .uri("/api/v3/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(
                        new UserAddressCreateDto(
                                new UserCreateDto(
                                        "joao@email.com",
                                        "123456",
                                        "João Henrique",
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
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(409);


    }

    @Test
    public void getUserCurrent_WithValidToken_ReturnResponseGlobalDtoStatus200(){
        ResponseGlobalDto responseDto = testClient
                .get()
                .uri("/api/v3/users/current")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseGlobalDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.user().id()).isEqualTo(400);
        org.assertj.core.api.Assertions.assertThat(responseDto.user().username()).isEqualTo("joao@email.com");
        org.assertj.core.api.Assertions.assertThat(responseDto.user().role()).isEqualTo(Role.ROLE_ADMIN);

        responseDto = testClient
                .get()
                .uri("/api/v3/users/current")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseGlobalDto.class)
                .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseDto.user().id()).isEqualTo(500);
            org.assertj.core.api.Assertions.assertThat(responseDto.user().username()).isEqualTo("maria@email.com");
            org.assertj.core.api.Assertions.assertThat(responseDto.user().role()).isEqualTo(Role.ROLE_CLIENT);

    }

    @Test
    public void recoverPassword_WithValidUsername_ReturnStringStatus200(){
        String responseDto = testClient
                .post()
                .uri("/api/v3/users/recover-password?username=joao@email.com")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();

    }

    @Test
    public void recoverPassword_WithInvalidUsername_ReturnErrorMessageStatus404(){
        ErrorMessage responseDto = testClient
                .post()
                .uri("/api/v3/users/recover-password?username=joao@email")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(404);

        responseDto = testClient
                .post()
                .uri("/api/v3/users/recover-password?username=joao@")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(404);

        responseDto = testClient
                .post()
                .uri("/api/v3/users/recover-password?username=")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(404);
    }

    @Test
    public void updateForgottenPassword_WithValidUsernameAndNewPasswordAndVerificationCode_ReturnStatus200(){
        testClient
                .put()
                .uri("/api/v3/users/recover-password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordRecoveryDto("joao@email.com", "654321", "ABCDEF"))
                .exchange()
                .expectStatus().isOk();

        testClient
                .put()
                .uri("/api/v3/users/recover-password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordRecoveryDto("maria@email.com", "654321", "ABCDEF"))
                .exchange()
                .expectStatus().isOk();

        testClient
                .put()
                .uri("/api/v3/users/recover-password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordRecoveryDto("carlos@email.com", "654321", "ABCDEF"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void updateForgottenPassword_WithInvalidUsername_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .put()
                .uri("/api/v3/users/recover-password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordRecoveryDto("", "654321", "ABCDEF"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);


        testClient
                .put()
                .uri("/api/v3/users/recover-password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordRecoveryDto("joao@email", "654321", "ABCDEF"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);


        testClient
                .put()
                .uri("/api/v3/users/recover-password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordRecoveryDto("joao@email", "654321", "ABCDEF"))
                .exchange()
                .expectStatus().isEqualTo(422);

        testClient
                .put()
                .uri("/api/v3/users/recover-password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordRecoveryDto("joao@", "654321", "ABCDEF"))
                .exchange()
                .expectStatus().isEqualTo(422);
    }

    @Test
    public void updateForgottenPassword_WithInvalidNewPassword_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .put()
                .uri("/api/v3/users/recover-password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordRecoveryDto("joao@email.com", "", "ABCDEF"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);


        testClient
                .put()
                .uri("/api/v3/users/recover-password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordRecoveryDto("joao@email.com", "123", "ABCDEF"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void updateForgottenPassword_WithInvalidVerificationCode_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .put()
                .uri("/api/v3/users/recover-password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordRecoveryDto("joao@email.com", "123456", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void changePassword_WithValidPasswordAndNwPassword_ReturnStatus200(){
        testClient
                .put()
                .uri("/api/v3/users/password")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordChangeDto("123456", "654321", "654321"))
                .exchange()
                .expectStatus().isOk();
        testClient
                .put()
                .uri("/api/v3/users/password")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordChangeDto("123456", "654321", "654321"))
                .exchange()
                .expectStatus().isOk();

        testClient
                .put()
                .uri("/api/v3/users/password")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordChangeDto("123456", "654321", "654321"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void changePassword_WithInvalidNewPassword_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .put()
                .uri("/api/v3/users/password")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordChangeDto("123456", "654", "654"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

        testClient
                .put()
                .uri("/api/v3/users/password")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordChangeDto("123456", "", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);


    }

    @Test
    public void changePassword_WithInvalidPassword_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .put()
                .uri("/api/v3/users/password")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordChangeDto("", "654321", "654321"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

        testClient
                .put()
                .uri("/api/v3/users/password")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "carlos@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordChangeDto("123", "654321", "654321"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void changePassword_WithInvalidNewPasswordAndConfirmPassword_ReturnErrorMessageStatus400(){
        ErrorMessage responseDto = testClient
                .put()
                .uri("/api/v3/users/password")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordChangeDto("123456", "123456789", "123456"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

                org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
                org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(400);
    }

    @Test
    public void changePassword_WithInvalidPassword_ReturnErrorMessageStatus400(){
        ErrorMessage responseDto = testClient
                .put()
                .uri("/api/v3/users/password")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordChangeDto("12345678", "123456", "123456"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(400);
    }


    @Test
    public void changeAddress_WithValidDatar_ReturnStatus200(){
        testClient
                .put()
                .uri("/api/v3/users/address")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new AddressCreateDto(
                                "06766-135",
                                "Alameda",
                                "Taboão da Serra",
                                18L,
                                "SP"
                        )
                )
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    public void changeAddress_WithInvalidZipCode_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .put()
                .uri("/api/v3/users/address")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new AddressCreateDto(
                                    "",
                                    "Alameda Joaquina",
                                    "Taboão da Serra",
                                    18L,
                                    "SP"
                            )

                )
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);
    }

    @Test
    public void changeAddress_WithInvalidStreet_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .put()
                .uri("/api/v3/users/address")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new AddressCreateDto(
                                    "06766-135",
                                    "",
                                    "Taboão da Serra",
                                    18L,
                                    "SP"
                            )
                )
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void changeAddress_WithInvalidCity_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .put()
                .uri("/api/v3/users/address")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new AddressCreateDto(
                                "06766-135",
                                "Alameda",
                                "",
                                18L,
                                "SP"
                        )
                )
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void changeAddress_WithInvalidNumber_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .put()
                .uri("/api/v3/users/address")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new AddressCreateDto(
                                "06766-135",
                                "Alameda",
                                "Taboão da Serra",
                                null,
                                "SP"
                        )
                )
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void changeAddress_WithInvalidUf_ReturnErrorMessageStatus422(){
        ErrorMessage responseDto = testClient
                .put()
                .uri("/api/v3/users/address")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new AddressCreateDto(
                                "06766-135",
                                "Alameda",
                                "Taboão da Serra",
                                18L,
                                ""
                        )
                )
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void deleteUser_WithValidToken_ReturnStatus200(){
        testClient
                .delete()
                .uri("/api/v3/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                .exchange()
                .expectStatus().isOk();
    }
}
