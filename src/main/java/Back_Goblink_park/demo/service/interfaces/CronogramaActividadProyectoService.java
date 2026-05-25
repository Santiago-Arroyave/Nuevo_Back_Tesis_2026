package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.CronogramaActividadProyectoRequest;
import Back_Goblink_park.demo.dto.response.CronogramaActividadProyectoResponse;

import java.util.List;

public interface CronogramaActividadProyectoService {

    // =====================================================
    // CREAR
    // =====================================================

    CronogramaActividadProyectoResponse
    crearActividad(
            CronogramaActividadProyectoRequest request
    );

    // =====================================================
    // LISTAR TODAS
    // =====================================================

    List<CronogramaActividadProyectoResponse>
    listarActividades();

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    CronogramaActividadProyectoResponse
    obtenerActividad(
            Long id
    );

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    List<CronogramaActividadProyectoResponse>
    listarPorProyecto(
            Long proyectoId
    );

    // =====================================================
    // LISTAR POR RESPONSABLE
    // =====================================================

    List<CronogramaActividadProyectoResponse>
    listarPorResponsable(
            Long responsableId
    );

    // =====================================================
    // LISTAR POR ESTADO
    // =====================================================

    List<CronogramaActividadProyectoResponse>
    listarPorEstado(
            String estado
    );

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    CronogramaActividadProyectoResponse
    actualizarActividad(
            Long id,
            CronogramaActividadProyectoRequest request
    );

    // =====================================================
    // ELIMINAR
    // =====================================================

    void eliminarActividad(
            Long id
    );
}