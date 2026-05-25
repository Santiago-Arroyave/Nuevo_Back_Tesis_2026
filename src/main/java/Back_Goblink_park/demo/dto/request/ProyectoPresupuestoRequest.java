package Back_Goblink_park.demo.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoPresupuestoRequest {

    // =====================================================
    // PROYECTO
    // =====================================================

    private Long proyectoId;

    // =====================================================
    // RUBRO
    // =====================================================

    private String rubro;

    // =====================================================
    // MONTO
    // =====================================================

    private BigDecimal monto;

    // =====================================================
    // OBSERVACIONES
    // =====================================================

    private String observaciones;
}