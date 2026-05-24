package Back_Goblink_park.demo.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CategoriaResponse {

    private Long id;

    private String nombre;

    private String descripcion;

    private Boolean estado;
}