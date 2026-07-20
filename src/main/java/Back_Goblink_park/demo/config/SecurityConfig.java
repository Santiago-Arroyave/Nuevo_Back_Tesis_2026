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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // =====================================================
    // CONFIGURACIÓN CORS - ÚNICA Y CENTRALIZADA ✅
    // =====================================================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ Permitir orígenes específicos del frontend (NO usar "*" con allowCredentials)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",   // Vite (Lovable por defecto)
                "http://localhost:3000",
                "http://localhost:8081", // React/Create-React-App
                "http://localhost:8080",   // Backend (desarrollo local)
                "http://127.0.0.1:5173",
                "http://127.0.0.1:3000",
                "http://127.0.0.1:8080",
                "https://goblinpark.onrender.com"
                // Agrega aquí tu dominio de producción cuando lo tengas:
                // "https://tu-app-lovable.app"
        ));

        // ✅ Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // ✅ Headers permitidos (incluyendo Authorization para JWT)
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        // ✅ Permitir credenciales (tokens JWT) ← ESTO ES LO QUE QUERÍAS
        configuration.setAllowCredentials(true);

        // ✅ Headers expuestos al frontend
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));

        // ✅ Tiempo de caché para preflight requests (1 hora)
        configuration.setMaxAge(3600L);

        // ✅ Aplicar configuración a todas las rutas de la API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // =====================================================
    // SECURITY FILTER CHAIN
    // =====================================================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // =====================================
                // HABILITAR CORS (usando el bean de arriba)
                // =====================================
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // =====================================
                // DESACTIVAR CSRF (para APIs stateless con JWT)
                // =====================================
                .csrf(csrf -> csrf.disable())

                // =====================================
                // SESSIONLESS JWT
                // =====================================
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // =====================================
                // AUTORIZACIÓN DE RUTAS
                // =====================================
                .authorizeHttpRequests(auth -> auth
                        // =====================================================
                        // AUTH PÚBLICO
                        // =====================================================
                        .requestMatchers("/api/auth/**").permitAll()

                        // =====================================================
                        // REPORTES - PATCH ESPECÍFICOS PARA ADMIN
                        // =====================================================
                        .requestMatchers(HttpMethod.PATCH, "/api/reportes/*/estado").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/reportes/*/prioridad").hasAuthority("ADMIN")

                        // =====================================================
                        // ROLES -> SOLO ADMIN
                        // =====================================================
                        .requestMatchers("/api/roles/**").hasAuthority("ADMIN")

                        // =====================================================
                        // USUARIOS -> SOLO ADMIN
                        // =====================================================
                        .requestMatchers("/api/usuarios/**").hasAuthority("ADMIN")

                        // =====================================================
                        // CATEGORÍAS
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/categorias/**").hasAuthority("ADMIN")

                        // =====================================================
                        // PRIORIDADES
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/prioridades/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/prioridades/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/prioridades/**").hasAuthority("ADMIN")

                        // =====================================================
                        // ESTADOS DE REPORTE
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/estados-reporte/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/estados-reporte/**").hasAuthority("ADMIN")

                        // =====================================================
                        // ESTADOS DE PROYECTO
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/estados-proyecto/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/estados-proyecto/**").hasAuthority("ADMIN")

                        // =====================================================
                        // REPORTES
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/reportes/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/reportes/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/reportes/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/reportes/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/reportes/**").hasAuthority("ADMIN")

                        // =====================================================
                        // EVIDENCIAS
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/evidencias/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/evidencias/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/evidencias/**").hasAuthority("ADMIN")

                        // =====================================================
                        // COMENTARIOS DE REPORTE
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/comentarios-reporte/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/comentarios-reporte/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/comentarios-reporte/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/comentarios-reporte/**").hasAuthority("ADMIN")

                        // =====================================================
                        // PROYECTOS
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/proyectos/*/detalle-completo").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/proyectos/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/proyectos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/proyectos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/proyectos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/proyectos/**").hasAuthority("ADMIN")

                        // =====================================================
                        // PROYECTO MIEMBROS
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/proyecto-miembros/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/proyecto-miembros/**").hasAuthority("ADMIN")

                        // =====================================================
                        // PROYECTO REPORTES
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/proyecto-reportes/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/proyecto-reportes/**").hasAuthority("ADMIN")

                        // =====================================================
                        // RESPONSABLES DE PROYECTO
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/responsables-proyecto/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/responsables-proyecto/**").hasAuthority("ADMIN")

                        // =====================================================
                        // SEGUIMIENTOS DE PROYECTO
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/seguimientos-proyecto/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/seguimientos-proyecto/**").hasAuthority("ADMIN")

                        // =====================================================
                        // CRONOGRAMA DE ACTIVIDADES
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/cronograma-actividades/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/cronograma-actividades/**").hasAuthority("ADMIN")

                        // =====================================================
                        // PROYECTO OBJETIVOS
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/proyecto-objetivos/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/proyecto-objetivos/**").hasAuthority("ADMIN")

                        // =====================================================
                        // PROYECTO METAS
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/proyecto-metas/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/proyecto-metas/**").hasAuthority("ADMIN")

                        // =====================================================
                        // PROYECTO PRESUPUESTOS
                        // =====================================================
                        .requestMatchers(HttpMethod.GET, "/api/proyecto-presupuestos/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/proyecto-presupuestos/**").hasAuthority("ADMIN")

                        // =====================================================
                        // TODO LO DEMÁS REQUIERE AUTENTICACIÓN
                        // =====================================================
                        .anyRequest().authenticated()
                )

                // =====================================
                // FILTRO JWT (antes de UsernamePasswordAuthenticationFilter)
                // =====================================
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // =====================================================
    // AUTHENTICATION MANAGER
    // =====================================================
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}