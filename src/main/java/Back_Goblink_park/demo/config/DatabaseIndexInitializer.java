package Back_Goblink_park.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Garantiza en el arranque la existencia de la columna client_uuid (idempotencia offline)
 * y de los índices B-Tree en PostgreSQL sobre reportes y proyecto_reportes para
 * consultas concurrentes de baja latencia desde la app móvil y el panel web.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseIndexInitializer implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        try {
            // 1. Asegurar columna client_uuid e índice único en reportes
            ejecutarSeguro("""
                ALTER TABLE reportes
                ADD COLUMN IF NOT EXISTS client_uuid VARCHAR(100)
            """);

            ejecutarSeguro("""
                CREATE UNIQUE INDEX IF NOT EXISTS idx_reportes_client_uuid
                ON reportes USING btree (client_uuid)
                WHERE client_uuid IS NOT NULL
            """);

            // 2. Inspeccionar columnas reales de la tabla reportes para crear índices B-Tree exactos
            Set<String> colsReportes = obtenerColumnasTabla("reportes");

            // Índice sobre usuario (id_usuario / usuario_id)
            crearIndiceSiExisteColumna(colsReportes, "reportes", "id_usuario", "idx_reportes_id_usuario");
            crearIndiceSiExisteColumna(colsReportes, "reportes", "usuario_id", "idx_reportes_usuario_id");

            // Índice sobre estado (id_estado_reporte / estado)
            crearIndiceSiExisteColumna(colsReportes, "reportes", "id_estado_reporte", "idx_reportes_id_estado_reporte");
            crearIndiceSiExisteColumna(colsReportes, "reportes", "estado", "idx_reportes_estado");

            // Índice sobre categoría (id_categoria / categoria_id / categoria)
            crearIndiceSiExisteColumna(colsReportes, "reportes", "id_categoria", "idx_reportes_id_categoria");
            crearIndiceSiExisteColumna(colsReportes, "reportes", "categoria_id", "idx_reportes_categoria_id");
            crearIndiceSiExisteColumna(colsReportes, "reportes", "categoria", "idx_reportes_categoria");

            // Índice sobre proyecto directo en reportes (si existe columna proyecto_id o id_proyecto)
            crearIndiceSiExisteColumna(colsReportes, "reportes", "proyecto_id", "idx_reportes_proyecto_id");
            crearIndiceSiExisteColumna(colsReportes, "reportes", "id_proyecto", "idx_reportes_id_proyecto");

            // Índice compuesto para listados paginados por eliminado + fecha_reporte
            if (colsReportes.contains("eliminado") && colsReportes.contains("fecha_reporte")) {
                ejecutarSeguro("""
                    CREATE INDEX IF NOT EXISTS idx_reportes_eliminado_fecha
                    ON reportes USING btree (eliminado, fecha_reporte DESC)
                """);
            }

            // 3. Índices B-Tree en tabla puente proyecto_reportes (id_proyecto, id_reporte)
            Set<String> colsProyectoReportes = obtenerColumnasTabla("proyecto_reportes");
            crearIndiceSiExisteColumna(colsProyectoReportes, "proyecto_reportes", "id_proyecto", "idx_proyecto_reportes_id_proyecto");
            crearIndiceSiExisteColumna(colsProyectoReportes, "proyecto_reportes", "proyecto_id", "idx_proyecto_reportes_proyecto_id");
            crearIndiceSiExisteColumna(colsProyectoReportes, "proyecto_reportes", "id_reporte", "idx_proyecto_reportes_id_reporte");
            crearIndiceSiExisteColumna(colsProyectoReportes, "proyecto_reportes", "reporte_id", "idx_proyecto_reportes_reporte_id");

            log.info("[DatabaseIndexInitializer] Índices B-Tree e idempotencia (client_uuid) verificados en PostgreSQL.");
        } catch (Exception e) {
            log.warn("[DatabaseIndexInitializer] No se pudieron verificar todos los índices al iniciar: {}", e.getMessage());
        }
    }

    private Set<String> obtenerColumnasTabla(String tableName) {
        try {
            List<String> cols = jdbcTemplate.queryForList("""
                SELECT LOWER(column_name)
                FROM information_schema.columns
                WHERE table_schema = 'public'
                  AND LOWER(table_name) = LOWER(?)
            """, String.class, tableName);
            return new HashSet<>(cols);
        } catch (Exception e) {
            return Set.of();
        }
    }

    private void crearIndiceSiExisteColumna(Set<String> columnas, String tabla, String columna, String nombreIndice) {
        if (columnas.contains(columna.toLowerCase())) {
            String sql = String.format(
                    "CREATE INDEX IF NOT EXISTS %s ON %s USING btree (%s)",
                    nombreIndice, tabla, columna
            );
            ejecutarSeguro(sql);
        }
    }

    private void ejecutarSeguro(String sql) {
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            log.debug("[DatabaseIndexInitializer] Omitiendo sentencia SQL ({}): {}", sql.trim(), e.getMessage());
        }
    }
}
