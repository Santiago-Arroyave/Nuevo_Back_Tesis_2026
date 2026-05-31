package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ProyectoObjetivoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoObjetivoResponse;

import java.util.List;

public interface ProyectoObjetivoService {

    ProyectoObjetivoResponse crearObjetivo(ProyectoObjetivoRequest request);

    List<ProyectoObjetivoResponse> listarObjetivosProyecto(Long proyectoId);

    ProyectoObjetivoResponse obtenerObjetivo(Long id);

    ProyectoObjetivoResponse actualizarObjetivo(Long id, ProyectoObjetivoRequest request);

    void eliminarObjetivo(Long id);
}