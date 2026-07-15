package Back_Goblink_park.demo.dto.request;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaActividadProyectoRequest {

    private Long id; // Para saber si es actualización

    private Long proyectoId;


    // =====================================================
    // RESPONSABLE - DOS OPCIONES
    // =====================================================

    // Opción 1: ID directo del miembro (cambia al editar proyecto)
    private Long proyectoMiembroId;

    // ✅ Opción 2: ID del usuario (NO cambia al editar proyecto)
    private Long usuarioId;

    // =====================================================
    // INFORMACIÓN
    // =====================================================
    private String nombre;
    private String descripcion;

    // =====================================================
    // ESTADO
    // =====================================================
    private String estado;

    // =====================================================
    // PRIORIDAD
    // =====================================================
    private String prioridad;

    // =====================================================
    // AVANCE
    // =====================================================
    private BigDecimal porcentajeAvance;

    // =====================================================
    // FECHAS
    // =====================================================
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // =====================================================
    // EVIDENCIA
    // =====================================================
    private String urlEvidencia;

    // =====================================================
    // OBSERVACIONES
    // =====================================================
    private String observaciones;
}