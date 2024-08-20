package com.swiss.wallet.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("security", securityScheme()))
                .info(
                        new Info()
                                .title("REST API - Swiss Wallet")
                                .description("Swiss Wallet is a digital wallet designed to promote " +
                                        "the importance of AAPM and force it to help manage social " +
                                        "activities to help students.")
                                .version("v2")
                                .contact(new Contact().name("Pedro Henrique").email("pedrohenriquefdasilva14@gmaiç.com"))
                );
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .description("Insira um bearer token valido para prosseguir") // Descrição do esquema de segurança
                .type(SecurityScheme.Type.HTTP) // Tipo de esquema de segurança (HTTP)
                .in(SecurityScheme.In.HEADER) // Localização do token no request (cabeçalho)
                .scheme("bearer") // Nome do esquema de segurança (Bearer Token)
                .bearerFormat("JWT") // Formato esperado do token (JWT)
                .name("security"); // Nome do esquema de segurança usado nas operações da API
    }

}
