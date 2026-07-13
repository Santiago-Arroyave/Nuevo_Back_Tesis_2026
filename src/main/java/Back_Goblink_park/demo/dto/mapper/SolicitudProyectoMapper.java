package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.SolicitudProyectoResponse;
import Back_Goblink_park.demo.entity.SolicitudProyecto;

public class SolicitudProyectoMapper {

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
}