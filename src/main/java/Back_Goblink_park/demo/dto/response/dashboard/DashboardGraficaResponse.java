package Back_Goblink_park.demo.dto.response.dashboard;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardGraficaResponse {

    private String nombre;
    private Long cantidad;
    private String color;
}