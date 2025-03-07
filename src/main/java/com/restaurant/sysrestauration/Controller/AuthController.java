package com.restaurant.sysrestauration.Controller;

import com.restaurant.sysrestauration.security.CustomUserDetails;
import com.restaurant.sysrestauration.security.JwtTokenProvider;
import com.restaurant.sysrestauration.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Authentification", description = "API pour l'authentification des utilisateurs")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Operation(summary = "Authentifier un utilisateur", description = "Permet à un utilisateur de se connecter et de récupérer un token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentification réussie",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Échec de l'authentification - Identifiants incorrects")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Authentification
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // Chargement des détails de l'utilisateur
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        // Génération du token
        String token = tokenProvider.generateToken(userDetails.getUsername());

        // Création de la réponse avec le token
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        // Retourne la réponse contenant le token
        return ResponseEntity.ok(response);
    }

    static class LoginRequest {
        @Schema(description = "Email de l'utilisateur", example = "user@example.com")
        private String email;

        @Schema(description = "Mot de passe de l'utilisateur", example = "password123")
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    static class AuthResponse {
        @Schema(description = "Token JWT généré après authentification", example = "eyJhbGciOiJIUzI1NiIsIn...")
        private String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}
