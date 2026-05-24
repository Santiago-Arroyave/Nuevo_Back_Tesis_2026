package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.response.CategoriaResponse;
import Back_Goblink_park.demo.dto.request.CategoriaRequest;


import java.util.List;

public interface CategoriaService {

    CategoriaResponse crear(
            CategoriaRequest request
    );

    List<CategoriaResponse> listar();

    CategoriaResponse obtenerPorId(
            Long id
    );

    CategoriaResponse actualizar(
            Long id,
            CategoriaRequest request
    );

    void eliminar(Long id);
}