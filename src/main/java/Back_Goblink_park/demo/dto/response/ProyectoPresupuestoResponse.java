package Back_Goblink_park.demo.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoPresupuestoResponse {

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

    // =====================================================
    // AUDITORÍA
    // =====================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}