package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.SolicitudProyectoRequest;
import Back_Goblink_park.demo.dto.request.SolicitudResponderRequest;
import Back_Goblink_park.demo.dto.response.SolicitudProyectoResponse;

import java.util.List;

public interface SolicitudProyectoService {

    // Usuario móvil: enviar solicitud
    SolicitudProyectoResponse crearSolicitud(SolicitudProyectoRequest request, String correoUsuario);

    // Admin: listar solicitudes pendientes de un proyecto
    List<SolicitudProyectoResponse> listarSolicitudesPendientes(Long proyectoId);

    // Admin: listar todas las solicitudes de un proyecto
    List<SolicitudProyectoResponse> listarSolicitudesPorProyecto(Long proyectoId);

    // Admin: listar todas las solicitudes pendientes (dashboard)
    List<SolicitudProyectoResponse> listarTodasPendientes();

    SolicitudProyectoResponse obtenerSolicitud(Long id);
    // Nuevos métodos separados:
    SolicitudProyectoResponse aceptarSolicitud(Long id, SolicitudResponderRequest request, String correoUsuario);

    SolicitudProyectoResponse rechazarSolicitud(Long id, SolicitudResponderRequest request, String correoUsuario);
}