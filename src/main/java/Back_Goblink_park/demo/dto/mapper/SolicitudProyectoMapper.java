package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.request.SolicitudResponderRequest;
import Back_Goblink_park.demo.dto.response.SolicitudProyectoResponse;
import Back_Goblink_park.demo.entity.SolicitudProyecto;
import Back_Goblink_park.demo.entity.Usuario;
import java.time.LocalDateTime;

public class SolicitudProyectoMapper {

    // 1. De Entidad a Response (El que ya tenías, está perfecto)
    public static SolicitudProyectoResponse toResponse(SolicitudProyecto solicitud) {
        return SolicitudProyectoResponse.builder()
                .id(solicitud.getId())
                .usuarioId(solicitud.getUsuario() != null ? solicitud.getUsuario().getId() : null)
                .usuarioNombre(solicitud.getUsuario() != null ? solicitud.getUsuario().getNombres() : null)
                .usuarioCorreo(solicitud.getUsuario() != null ? solicitud.getUsuario().getCorreo() : null)
                .proyectoId(solicitud.getProyecto() != null ? solicitud.getProyecto().getId() : null)
                .proyectoNombre(solicitud.getProyecto() != null ? solicitud.getProyecto().getNombre() : null)
                .estado(solicitud.getEstado())
                .mensaje(solicitud.getMensaje())
                .respuesta(solicitud.getRespuesta())
                .respondidoPorId(solicitud.getRespondidoPor() != null ? solicitud.getRespondidoPor().getId() : null)
                .respondidoPorNombre(solicitud.getRespondidoPor() != null ? solicitud.getRespondidoPor().getNombres() : null)
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .fechaRespuesta(solicitud.getFechaRespuesta())
                .build();
    }

    // 2. NUEVO: Actualizar la entidad desde el Request de respuesta
    public static void updateEntityFromRequest(SolicitudProyecto solicitud, SolicitudResponderRequest request, Usuario usuarioQueResponde) {
        solicitud.setEstado(request.getEstado()); // "ACEPTADA" o "RECHAZADA"
        solicitud.setRespuesta(request.getRespuesta());
        solicitud.setRespondidoPor(usuarioQueResponde);
        solicitud.setFechaRespuesta(LocalDateTime.now());

        // NOTA: El campo 'rolEnProyecto' normalmente se guarda en una tabla intermedia
        // (ej: ProyectoMiembro) si la solicitud es aceptada, no en la tabla de solicitudes.
        // Lo dejo comentado para que lo descomentes si tu entidad SolicitudProyecto tiene ese campo.
        // if ("ACEPTADA".equalsIgnoreCase(request.getEstado())) {
        //     solicitud.setRolEnProyecto(request.getRolEnProyecto());
        // }
    }
}