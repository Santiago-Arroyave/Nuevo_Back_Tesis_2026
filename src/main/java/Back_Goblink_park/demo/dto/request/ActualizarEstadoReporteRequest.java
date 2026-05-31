package Back_Goblink_park.demo.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarEstadoReporteRequest {

    // =========================================
    // NUEVO ESTADO
    // =========================================

    private Long estadoReporteId;

    // =========================================
    // OBSERVACIONES ADMIN
    // =========================================

    private String observacionesAdmin;

}