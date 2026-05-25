package Back_Goblink_park.demo.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeguimientoProyectoResponse {

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
    // USUARIO
    // =====================================================

    private Long usuarioId;

    private String usuarioNombre;

    // =====================================================
    // AVANCE
    // =====================================================

    private String descripcionAvance;

    // =====================================================
    // PORCENTAJE
    // =====================================================

    private BigDecimal porcentajeAvance;

    // =====================================================
    // FECHA SEGUIMIENTO
    // =====================================================

    private LocalDateTime fechaSeguimiento;

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