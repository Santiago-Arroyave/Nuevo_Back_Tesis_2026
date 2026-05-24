package Back_Goblink_park.demo.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EstadoReporteRequest {

    private String nombre;

    private String descripcion;

    private Boolean estado;

    private String color;

    private Short ordenVisual;

}
