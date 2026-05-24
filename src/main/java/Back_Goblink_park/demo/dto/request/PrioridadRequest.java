package Back_Goblink_park.demo.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrioridadRequest {

    private String nombre;

    private Short nivel;

    private String descripcion;

    private Boolean estado;
}