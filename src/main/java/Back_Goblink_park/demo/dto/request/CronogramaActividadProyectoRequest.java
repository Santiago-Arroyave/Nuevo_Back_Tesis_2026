package Back_Goblink_park.demo.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaActividadProyectoRequest {

    // =====================================================
    // PROYECTO
    // =====================================================

    private Long proyectoId;

    // =====================================================
    // RESPONSABLE
    // =====================================================

    private Long responsableId;

    // =====================================================
    // INFORMACIÓN
    // =====================================================

    private String nombre;

    private String descripcion;

    // =====================================================
    // ESTADO
    // =====================================================

    /*
        pendiente
        en_progreso
        completada
        cancelada
     */

    private String estado;

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