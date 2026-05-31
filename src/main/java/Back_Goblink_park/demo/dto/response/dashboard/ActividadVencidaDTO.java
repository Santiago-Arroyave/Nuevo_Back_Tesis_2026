package Back_Goblink_park.demo.dto.response.dashboard;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ActividadVencidaDTO {
    private Long id;
    private String nombreActividad;
    private String proyectoNombre;
    private LocalDate fechaLimite;
    private Integer diasRetraso;
    private String responsableNombre;
    private Long proyectoId;
}