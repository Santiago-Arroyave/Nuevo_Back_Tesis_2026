package Back_Goblink_park.demo.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudProyectoResponse {

    private Long id;

    // Alias para compatibilidad con clientes móviles
    public Long getIdSolicitud() {
        return this.id;
    }

    // Usuario solicitante
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioCorreo;

    // Proyecto
    private Long proyectoId;
    private String proyectoNombre;
    private ProyectoResponse proyecto;

    // Estado
    private String estado;

    // Mensaje y respuesta
    private String mensaje;
    private String respuesta;

    // Admin que respondió
    private Long respondidoPorId;
    private String respondidoPorNombre;

    // Fechas
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaRespuesta;
}