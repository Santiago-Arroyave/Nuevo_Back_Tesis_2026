package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ProyectoObjetivoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoObjetivoResponse;

import java.util.List;

public interface ProyectoObjetivoService {

    // =====================================================
    // CREAR OBJETIVO
    // =====================================================

    ProyectoObjetivoResponse crearObjetivo(
            ProyectoObjetivoRequest request
    );

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    List<ProyectoObjetivoResponse> listarObjetivos();

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    ProyectoObjetivoResponse obtenerObjetivo(
            Long id
    );

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    List<ProyectoObjetivoResponse> listarPorProyecto(
            Long proyectoId
    );

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    ProyectoObjetivoResponse actualizarObjetivo(
            Long id,
            ProyectoObjetivoRequest request
    );

    // =====================================================
    // ELIMINAR
    // =====================================================

    void eliminarObjetivo(
            Long id
    );
}