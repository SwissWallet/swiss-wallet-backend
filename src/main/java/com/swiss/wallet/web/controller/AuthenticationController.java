package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.TokenNotification;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.exception.InvalidCredencialException;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.jwt.JwtToken;
import com.swiss.wallet.jwt.JwtUserDetailsService;
import com.swiss.wallet.repository.ITokenRepository;
import com.swiss.wallet.repository.IUserRepository;
import com.swiss.wallet.web.dto.UserLoginDto;
import com.swiss.wallet.web.dto.UserLoginMobileDto;
import com.swiss.wallet.web.dto.UserResponseDto;
import com.swiss.wallet.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Resource to proceed with API authentication")
@RestController
@RequestMapping("/api/v3")
public class AuthenticationController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;
    private final ITokenRepository iTokenRepository;
    private final IUserRepository userRepository;

    public AuthenticationController(JwtUserDetailsService detailsService, AuthenticationManager authenticationManager, ITokenRepository iTokenRepository, IUserRepository userRepository) {
        this.detailsService = detailsService;
        this.authenticationManager = authenticationManager;
        this.iTokenRepository = iTokenRepository;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Authenticate to API", description = "API authentication feature",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful authentication and return of a bearer token",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid field(s)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authentication(@RequestBody @Valid UserLoginDto dto, HttpServletRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.username(), dto.password());

            authenticationManager.authenticate(authenticationToken);

            JwtToken token = detailsService.getTokenAuthenticated(dto.username());

            return ResponseEntity.ok(token);
        }catch (AuthenticationException ex) {
            throw new InvalidCredencialException("Invalid credencial " + dto.username());
        }
    }

    @Operation(summary = "Authenticate to API", description = "API authentication feature",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful authentication and return of a bearer token",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid field(s)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping(path = "/auth/mobile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticationMobile(@RequestBody @Valid UserLoginMobileDto dto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.username(), dto.password());

            authenticationManager.authenticate(authenticationToken);

            JwtToken token = detailsService.getTokenAuthenticated(dto.username());

            if(!token.getToken().isEmpty()){
                UserEntity user = userRepository.findByUsername(dto.username())
                        .orElseThrow(
                                () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                        );
                TokenNotification tokenNotification = new TokenNotification();
                tokenNotification.setUser(user);
                tokenNotification.setToken(dto.token());
                iTokenRepository.save(tokenNotification);
            }
            return ResponseEntity.ok(token);
        }catch (AuthenticationException ex) {
            throw new InvalidCredencialException("Invalid credencial " + dto.username());
        }
    }

}
