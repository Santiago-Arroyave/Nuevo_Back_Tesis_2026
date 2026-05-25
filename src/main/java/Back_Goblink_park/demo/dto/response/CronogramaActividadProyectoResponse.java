package Back_Goblink_park.demo.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaActividadProyectoResponse {

    // =====================================================
    // ID
    // =====================================================

    private Long id;

    // =====================================================
    // PROYECTO
    // =====================================================

    private Long proyectoId;

    private String proyectoNombre;

    // =====================================================
    // RESPONSABLE PROYECTO
    // =====================================================

    private Long responsableId;

    private String responsableTitulo;

    // =====================================================
    // USUARIO RESPONSABLE
    // =====================================================

    private Long usuarioResponsableId;

    private String usuarioResponsableNombre;

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

    // =====================================================
    // AUDITORÍA
    // =====================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}