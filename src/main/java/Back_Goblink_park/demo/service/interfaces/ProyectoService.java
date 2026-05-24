package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ProyectoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoResponse;

import java.util.List;

public interface ProyectoService {

    // =====================================================
    // CREAR
    // =====================================================

    ProyectoResponse crearProyecto(
            ProyectoRequest request,
            String correoUsuario
    );

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    List<ProyectoResponse> listarProyectos();

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    ProyectoResponse obtenerProyecto(
            Long id
    );

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    ProyectoResponse actualizarProyecto(
            Long id,
            ProyectoRequest request
    );

    // =====================================================
    // ELIMINADO LÓGICO
    // =====================================================

    void eliminarProyecto(
            Long id
    );

    // =====================================================
    // FILTRAR POR ESTADO PROYECTO
    // =====================================================

    List<ProyectoResponse> listarPorEstadoProyecto(
            Long estadoProyectoId
    );

    // =====================================================
    // FILTRAR POR PRIORIDAD
    // =====================================================

    List<ProyectoResponse> listarPorPrioridad(
            Long prioridadId
    );

    // =====================================================
    // FILTRAR POR CATEGORÍA
    // =====================================================

    List<ProyectoResponse> listarPorCategoria(
            Long categoriaId
    );

    // =====================================================
    // FILTRAR POR USUARIO
    // =====================================================

    List<ProyectoResponse> listarPorUsuario(
            Long usuarioId
    );

    // =====================================================
    // BUSCAR POR NOMBRE
    // =====================================================

    List<ProyectoResponse> buscarPorNombre(
            String nombre
    );
}