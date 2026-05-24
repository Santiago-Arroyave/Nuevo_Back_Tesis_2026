package Back_Goblink_park.demo.config;

import Back_Goblink_park.demo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // =====================================================
    // SECURITY FILTER CHAIN
    // =====================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // =====================================
                // DESACTIVAR CSRF
                // =====================================

                .csrf(csrf -> csrf.disable())

                // =====================================
                // SESSIONLESS JWT
                // =====================================

                .sessionManagement(session ->

                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // =====================================
                // AUTORIZACIÓN
                // =====================================

                .authorizeHttpRequests(auth -> auth

                        // =====================================
                        // AUTH LIBRE
                        // =====================================

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // =====================================================
                        // ROLES
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/roles/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                "/api/roles/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // USUARIOS
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/usuarios/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/usuarios/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/usuarios/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // CATEGORIAS
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/categorias/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/categorias/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // PRIORIDADES
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/prioridades/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/prioridades/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // ESTADOS REPORTE
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/estados-reporte/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/estados-reporte/**"
                        ).hasAuthority("ADMIN")

                                // =====================================================
                                // ESTADOS PROYECTO
                                // =====================================================

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/estados-proyecto/**"
                                ).hasAnyAuthority("ADMIN", "USER")

                                .requestMatchers(
                                        "/api/estados-proyecto/**"
                                ).hasAuthority("ADMIN")


                                // =====================================
                                // REPORTES
                                // GET -> USER Y ADMIN
                                // =====================================

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/reportes/**"
                                ).hasAnyAuthority("ADMIN", "USER")

                                // =====================================
                                // REPORTES
                                // POST -> USER Y ADMIN
                                // =====================================

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/reportes/**"
                                ).hasAnyAuthority("ADMIN", "USER")

                                // =====================================
                                // REPORTES
                                // DELETE -> SOLO ADMIN
                                // =====================================

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/reportes/**"
                                ).hasAuthority("ADMIN")
                                // =====================================
// EVIDENCIAS
// GET -> USER Y ADMIN
// =====================================

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/evidencias/**"
                                ).hasAnyAuthority("ADMIN", "USER")

// =====================================
// EVIDENCIAS
// POST -> USER Y ADMIN
// =====================================

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/evidencias/**"
                                ).hasAnyAuthority("ADMIN", "USER")

// =====================================
// EVIDENCIAS
// DELETE -> SOLO ADMIN
// =====================================

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/evidencias/**"
                                ).hasAuthority("ADMIN")

                                // =====================================
// COMENTARIOS
// GET -> USER Y ADMIN
// =====================================

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/comentarios/**"
                                ).hasAnyAuthority("ADMIN", "USER")

// =====================================
// COMENTARIOS
// POST -> USER Y ADMIN
// =====================================

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/comentarios/**"
                                ).hasAnyAuthority("ADMIN", "USER")

// =====================================
// COMENTARIOS
// PUT -> USER Y ADMIN
// =====================================

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/comentarios/**"
                                ).hasAnyAuthority("ADMIN", "USER")

// =====================================
// COMENTARIOS
// DELETE -> SOLO ADMIN
// =====================================

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/comentarios/**"
                                ).hasAuthority("ADMIN")

                                // PROYECTOS GET
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/proyectos/**"
                                ).hasAnyAuthority("ADMIN", "USER")

// PROYECTOS ADMIN
                                .requestMatchers(
                                        "/api/proyectos/**"
                                ).hasAuthority("ADMIN")
                        // =====================================
                        // TODO LO DEMÁS
                        // =====================================

                        .anyRequest()
                        .authenticated()
                )

                // =====================================
                // FILTRO JWT
                // =====================================

                .addFilterBefore(

                        jwtAuthenticationFilter,

                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // =====================================================
    // AUTH MANAGER
    // =====================================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }
}