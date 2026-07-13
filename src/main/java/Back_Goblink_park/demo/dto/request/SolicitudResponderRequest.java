package Back_Goblink_park.demo.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudResponderRequest {

    // respuesta: aceptada o rechazada
    private String estado;

    // Motivo de la respuesta
    private String respuesta;
}