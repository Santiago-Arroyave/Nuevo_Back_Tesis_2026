package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.PrioridadRequest;
import Back_Goblink_park.demo.dto.response.PrioridadResponse;

import java.util.List;

public interface PrioridadService {

    PrioridadResponse crear(PrioridadRequest request);

    List<PrioridadResponse> listar();

    PrioridadResponse obtener(Long id);

    PrioridadResponse actualizar(Long id, PrioridadRequest request);

    void eliminar(Long id);
}