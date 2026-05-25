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

                        // =====================================================
                        // AUTH LIBRE
                        // =====================================================

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // =====================================================
                        // ROLES -> SOLO ADMIN
                        // =====================================================

                        .requestMatchers(
                                "/api/roles/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // USUARIOS -> SOLO ADMIN
                        // =====================================================

                        .requestMatchers(
                                "/api/usuarios/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // CATEGORIAS
                        // GET -> ADMIN Y USER
                        // POST PUT DELETE -> ADMIN
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
                        // GET -> ADMIN Y USER
                        // POST PUT DELETE -> ADMIN
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
                        // GET -> ADMIN Y USER
                        // POST PUT DELETE -> ADMIN
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
                        // GET -> ADMIN Y USER
                        // POST PUT DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/estados-proyecto/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/estados-proyecto/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // REPORTES
                        // GET POST -> ADMIN Y USER
                        // PUT DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/reportes/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/reportes/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/reportes/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/reportes/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // EVIDENCIAS
                        // GET POST -> ADMIN Y USER
                        // DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/evidencias/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/evidencias/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/evidencias/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // COMENTARIOS REPORTE
                        // GET POST PUT -> ADMIN Y USER
                        // DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/comentarios-reporte/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/comentarios-reporte/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/comentarios-reporte/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/comentarios-reporte/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // PROYECTOS
                        // GET -> ADMIN Y USER
                        // POST PUT DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/proyectos/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/proyectos/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // PROYECTO MIEMBROS
                        // GET -> ADMIN Y USER
                        // POST DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/proyecto-miembros/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/proyecto-miembros/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // PROYECTO REPORTES
                        // GET -> ADMIN Y USER
                        // POST DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/proyecto-reportes/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/proyecto-reportes/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // RESPONSABLES PROYECTO
                        // GET -> ADMIN Y USER
                        // POST PUT DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/responsables-proyecto/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/responsables-proyecto/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // SEGUIMIENTOS PROYECTO
                        // GET -> ADMIN Y USER
                        // POST PUT DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/seguimientos-proyecto/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/seguimientos-proyecto/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // CRONOGRAMA ACTIVIDADES
                        // GET -> ADMIN Y USER
                        // POST PUT DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/cronograma-actividades/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/cronograma-actividades/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // PROYECTO OBJETIVOS
                        // GET -> ADMIN Y USER
                        // POST PUT DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/proyecto-objetivos/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/proyecto-objetivos/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // PROYECTO METAS
                        // GET -> ADMIN Y USER
                        // POST PUT DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/proyecto-metas/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/proyecto-metas/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // PROYECTO PRESUPUESTOS
                        // GET -> ADMIN Y USER
                        // POST PUT DELETE -> ADMIN
                        // =====================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/proyecto-presupuestos/**"
                        ).hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(
                                "/api/proyecto-presupuestos/**"
                        ).hasAuthority("ADMIN")

                        // =====================================================
                        // TODO LO DEMÁS PROTEGIDO
                        // =====================================================

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