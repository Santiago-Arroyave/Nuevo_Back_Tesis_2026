package Back_Goblink_park.demo.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoReporteRequest {

    // =====================================================
    // PROYECTO
    // =====================================================

    private Long proyectoId;

    // =====================================================
    // REPORTE
    // =====================================================

    private Long reporteId;
}