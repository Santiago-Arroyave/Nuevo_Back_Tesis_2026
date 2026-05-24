package Back_Goblink_park.demo.dto.response;

import lombok.*;

@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor

public class EstadoProyectoResponse {

    private Long id;

    private String nombre;

    private String descripcion;

    private String color;

    private Short ordenVisual;

    private Boolean estado;
}