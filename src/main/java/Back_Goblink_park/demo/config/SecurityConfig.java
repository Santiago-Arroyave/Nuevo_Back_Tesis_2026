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
                        // 🔓 RUTAS PÚBLICAS
                        .requestMatchers("/api/auth/**").permitAll()

                        // 🔒 MODIFICACIONES ADMINISTRATIVAS DIRECTAS
                        .requestMatchers(HttpMethod.PATCH, "/api/reportes/*/estado").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/reportes/*/prioridad").hasAuthority("ADMIN")
                        .requestMatchers("/api/roles/**").hasAuthority("ADMIN")

                        // ✅ PERFIL PROPIO (GET Y PUT)
                        .requestMatchers("/api/usuarios/perfil/**").authenticated()
                        .requestMatchers("/api/usuarios/perfil").authenticated()

                        // ✅ USUARIOS: Listado y estadísticas para usuarios autenticados
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/*/estadisticas").authenticated()
                        .requestMatchers("/api/usuarios/**").hasAuthority("ADMIN")

                        // ✅ CATÁLOGOS BASE (Cualquier usuario autenticado puede consultarlos)
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").authenticated()
                        .requestMatchers("/api/categorias/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/prioridades/**").authenticated()
                        .requestMatchers("/api/prioridades/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/estados-reporte/**").authenticated()
                        .requestMatchers("/api/estados-reporte/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/estados-proyecto/**").authenticated()
                        .requestMatchers("/api/estados-proyecto/**").hasAuthority("ADMIN")

                        // 🚀 GESTIÓN DE REPORTES (CORREGIDO)
                        .requestMatchers(HttpMethod.GET, "/api/reportes/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/reportes/**").authenticated()
                        // 👉 PUT: Permitir a usuarios autenticados (el Service valida si es autor o ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/reportes/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/reportes/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/reportes/**").hasAnyAuthority("ADMIN", "USER")

                        // ✅ EVIDENCIAS Y COMENTARIOS
                        .requestMatchers(HttpMethod.GET, "/api/evidencias/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/evidencias/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/evidencias/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/comentarios-reporte/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/comentarios-reporte/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/comentarios-reporte/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/comentarios-reporte/**").hasAuthority("ADMIN")

                        // ✅ PROYECTOS Y SEGUIMIENTO
                        .requestMatchers(HttpMethod.GET, "/api/proyectos/*/detalle-completo").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/proyectos/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/proyectos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/proyectos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/proyectos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/proyectos/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/proyecto-miembros/**").authenticated()
                        .requestMatchers("/api/proyecto-miembros/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/proyecto-reportes/**").authenticated()
                        .requestMatchers("/api/proyecto-reportes/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/responsables-proyecto/**").authenticated()
                        .requestMatchers("/api/responsables-proyecto/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/seguimientos-proyecto/**").authenticated()
                        .requestMatchers("/api/seguimientos-proyecto/**").hasAuthority("ADMIN")

                        // ✅ SOLICITUDES DE PROYECTO
                        .requestMatchers(HttpMethod.POST, "/api/solicitudes-proyecto").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes-proyecto/mis-solicitudes").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/solicitudes-proyecto/**").authenticated()
                        .requestMatchers("/api/solicitudes-proyecto/**").hasAuthority("ADMIN")

                        // ✅ CRONOGRAMA DE ACTIVIDADES
                        .requestMatchers(HttpMethod.GET, "/api/cronograma-actividades/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/cronograma-actividades/*/evidencias").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/cronograma-actividades/*/completar").authenticated()
                        .requestMatchers("/api/cronograma-actividades/**").hasAuthority("ADMIN")

                        // ✅ EVIDENCIAS DE ACTIVIDAD
                        .requestMatchers(HttpMethod.DELETE, "/api/evidencias-actividad/**").authenticated()
                        .requestMatchers("/api/evidencias-actividad/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/proyecto-objetivos/**").authenticated()
                        .requestMatchers("/api/proyecto-objetivos/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/proyecto-metas/**").authenticated()
                        .requestMatchers("/api/proyecto-metas/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/proyecto-presupuestos/**").authenticated()
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