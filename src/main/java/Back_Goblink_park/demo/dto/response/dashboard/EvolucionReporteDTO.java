package Back_Goblink_park.demo.dto.response.dashboard;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EvolucionReporteDTO {
    private LocalDate fecha;
    private Long cantidad;
}