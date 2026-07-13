package Back_Goblink_park.demo.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudProyectoRequest {

    // ID del proyecto al que quiere ingresar
    private Long proyectoId;

    // Mensaje opcional del usuario
    private String mensaje;
}