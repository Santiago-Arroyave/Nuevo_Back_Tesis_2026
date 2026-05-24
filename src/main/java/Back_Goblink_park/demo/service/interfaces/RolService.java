package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.RolRequest;
import Back_Goblink_park.demo.dto.response.RolResponse;

import java.util.List;

public interface RolService {

    RolResponse crearRol(RolRequest request);

    RolResponse obtenerPorId(Long id);

    List<RolResponse> listarRoles();

    RolResponse actualizarRol(Long id, RolRequest request);

    void eliminarRol(Long id);
}