package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.EstadoReporteRequest;
import Back_Goblink_park.demo.dto.response.EstadoReporteResponse;

import java.util.List;

public interface EstadoReporteService {

    EstadoReporteResponse crear(
            EstadoReporteRequest request
    );

    List<EstadoReporteResponse> listar();

    EstadoReporteResponse obtener(Long id);

    EstadoReporteResponse actualizar(
            Long id,
            EstadoReporteRequest request
    );

    void eliminar(Long id);
}