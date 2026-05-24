package Back_Goblink_park.demo.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrioridadResponse {

    private Long id;

    private String nombre;

    private Short nivel;

    private String descripcion;

    private Boolean estado;
}