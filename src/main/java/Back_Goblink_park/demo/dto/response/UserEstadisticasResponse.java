package Back_Goblink_park.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEstadisticasResponse {
    private Long totalProyectos;
    private Long totalReportes;
    private Long totalEvidencias;
}
