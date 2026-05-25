package Back_Goblink_park.demo.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeguimientoProyectoRequest {

    // =====================================================
    // PROYECTO
    // =====================================================

    private Long proyectoId;

    // =====================================================
    // USUARIO
    // =====================================================

    private Long usuarioId;

    // =====================================================
    // DESCRIPCIÓN AVANCE
    // =====================================================

    private String descripcionAvance;

    // =====================================================
    // PORCENTAJE AVANCE
    // =====================================================

    private BigDecimal porcentajeAvance;

    // =====================================================
    // OBSERVACIONES
    // =====================================================

    private String observaciones;
}