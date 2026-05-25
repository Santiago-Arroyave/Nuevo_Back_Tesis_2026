package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ResponsableProyectoRequest;

import Back_Goblink_park.demo.dto.response.ResponsableProyectoResponse;

import java.util.List;

public interface ResponsableProyectoService {

    // =====================================================
    // CREAR RESPONSABLE / TAREA
    // =====================================================

    ResponsableProyectoResponse crearResponsable(
            ResponsableProyectoRequest request
    );

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    List<ResponsableProyectoResponse>
    listarResponsables();

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    ResponsableProyectoResponse obtenerResponsable(
            Long id
    );

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    ResponsableProyectoResponse actualizarResponsable(
            Long id,
            ResponsableProyectoRequest request
    );

    // =====================================================
    // ELIMINAR
    // =====================================================

    void eliminarResponsable(
            Long id
    );

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    List<ResponsableProyectoResponse>
    listarPorProyecto(
            Long proyectoId
    );

    // =====================================================
    // LISTAR POR RESPONSABLE
    // =====================================================

    List<ResponsableProyectoResponse>
    listarPorUsuarioResponsable(
            Long usuarioId
    );

    // =====================================================
    // LISTAR POR ESTADO
    // =====================================================

    List<ResponsableProyectoResponse>
    listarPorEstado(
            String estado
    );

    // =====================================================
    // LISTAR POR PRIORIDAD
    // =====================================================

    List<ResponsableProyectoResponse>
    listarPorPrioridad(
            String prioridad
    );
}