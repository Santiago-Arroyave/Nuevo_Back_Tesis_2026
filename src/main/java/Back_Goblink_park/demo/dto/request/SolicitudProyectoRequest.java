package Back_Goblink_park.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudProyectoRequest {

    // ID del proyecto al que quiere ingresar
    @JsonAlias({"id_proyecto", "idProyecto"})
    private Long proyectoId;

    // Mensaje opcional del usuario
    private String mensaje;
}