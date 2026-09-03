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

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:3000",
                "http://localhost:8081",
                "http://localhost:8080",
                "http://127.0.0.1:5173",
                "http://127.0.0.1:3000",
                "http://127.0.0.1:8080",
                "https://goblinpark.onrender.com"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.PATCH, "/api/reportes/*/estado").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/reportes/*/prioridad").hasAuthority("ADMIN")

                        .requestMatchers("/api/roles/**").hasAuthority("ADMIN")

                        // ✅ 1. PERFIL PROPIO: Permitir a cualquier usuario autenticado (DEBE IR ANTES DEL /**)
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/perfil").authenticated()

                        // ✅ 2. LISTAR USUARIOS: Permitir a ADMIN y USER (DEBE IR ANTES DEL /**)
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAnyAuthority("ADMIN", "USER")

                        // ✅ 3. RESTO DE USUARIOS: Solo ADMIN (El /** captura el resto: POST, GET /{id}, PUT /{id}, DELETE, etc.)
                        .requestMatchers("/api/usuarios/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/categorias/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/prioridades/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/prioridades/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/estados-reporte/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/estados-reporte/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/estados-proyecto/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/estados-proyecto/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/reportes/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/reportes/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/reportes/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/reportes/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/reportes/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/evidencias/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/evidencias/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/evidencias/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/comentarios-reporte/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/comentarios-reporte/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/comentarios-reporte/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/comentarios-reporte/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/proyectos/*/detalle-completo").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/proyectos/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/proyectos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/proyectos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/proyectos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/proyectos/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/proyecto-miembros/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/proyecto-miembros/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/proyecto-reportes/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/proyecto-reportes/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/responsables-proyecto/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/responsables-proyecto/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/seguimientos-proyecto/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/seguimientos-proyecto/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/cronograma-actividades/*/completar").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/cronograma-actividades/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/proyecto-objetivos/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/proyecto-objetivos/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/proyecto-metas/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/proyecto-metas/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/proyecto-presupuestos/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/proyecto-presupuestos/**").hasAuthority("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}