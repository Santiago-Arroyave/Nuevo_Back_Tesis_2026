package Back_Goblink_park.demo.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsableProyectoRequest {

    // =====================================================
    // PROYECTO
    // =====================================================

    private Long proyectoId;

    // =====================================================
    // USUARIO RESPONSABLE
    // =====================================================

    private Long usuarioResponsableId;

    // =====================================================
    // INFORMACIÓN
    // =====================================================

    private String titulo;

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
    // PRIORIDAD
    // =====================================================

    /*
        baja
        media
        alta
     */

    private String prioridad;

    // =====================================================
    // FECHAS
    // =====================================================

    private LocalDate fechaInicio;

    private LocalDate fechaLimite;

    // =====================================================
    // AVANCE
    // =====================================================

    private BigDecimal porcentajeAvance;
}