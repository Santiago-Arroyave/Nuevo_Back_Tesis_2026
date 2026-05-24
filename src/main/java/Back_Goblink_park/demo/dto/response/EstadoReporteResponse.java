package Back_Goblink_park.demo.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoReporteResponse {

    private Long id;

    private String nombre;

    private String descripcion;

    private String color;

    private Short ordenVisual;

    private Boolean estado;
}