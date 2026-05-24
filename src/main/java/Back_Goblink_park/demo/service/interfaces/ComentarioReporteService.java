package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ComentarioReporteRequest;
import Back_Goblink_park.demo.dto.response.ComentarioReporteResponse;

import java.util.List;

public interface ComentarioReporteService {

    ComentarioReporteResponse crearComentario(
            ComentarioReporteRequest request,
            String correoUsuario
    );

    List<ComentarioReporteResponse> listarComentarios();

    ComentarioReporteResponse obtenerComentario(Long id);

    ComentarioReporteResponse actualizarComentario(
            Long id,
            ComentarioReporteRequest request
    );

    List<ComentarioReporteResponse> listarPorReporte(
            Long reporteId
    );

    List<ComentarioReporteResponse> listarPorUsuario(
            Long usuarioId
    );

    void eliminarComentario(Long id);
}