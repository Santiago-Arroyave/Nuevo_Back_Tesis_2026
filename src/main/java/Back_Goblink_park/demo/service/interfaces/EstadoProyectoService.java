package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.EstadoProyectoRequest;
import Back_Goblink_park.demo.dto.response.EstadoProyectoResponse;

import java.util.List;

public interface EstadoProyectoService {

    EstadoProyectoResponse crear(
            EstadoProyectoRequest request
    );

    List<EstadoProyectoResponse> listar();

    EstadoProyectoResponse obtenerPorId(Long id);

    EstadoProyectoResponse actualizar(
            Long id,
            EstadoProyectoRequest request
    );

    void eliminar(Long id);
}