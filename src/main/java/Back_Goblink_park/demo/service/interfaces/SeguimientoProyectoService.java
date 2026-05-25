package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.SeguimientoProyectoRequest;

import Back_Goblink_park.demo.dto.response.SeguimientoProyectoResponse;

import java.util.List;

public interface SeguimientoProyectoService {

    // =====================================================
    // CREAR SEGUIMIENTO
    // =====================================================

    SeguimientoProyectoResponse crearSeguimiento(
            SeguimientoProyectoRequest request
    );

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    List<SeguimientoProyectoResponse>
    listarSeguimientos();

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    SeguimientoProyectoResponse obtenerSeguimiento(
            Long id
    );

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    SeguimientoProyectoResponse actualizarSeguimiento(
            Long id,
            SeguimientoProyectoRequest request
    );

    // =====================================================
    // ELIMINAR
    // =====================================================

    void eliminarSeguimiento(
            Long id
    );

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    List<SeguimientoProyectoResponse>
    listarPorProyecto(
            Long proyectoId
    );

    // =====================================================
    // LISTAR POR USUARIO
    // =====================================================

    List<SeguimientoProyectoResponse>
    listarPorUsuario(
            Long usuarioId
    );

    // =====================================================
    // LISTAR POR PROYECTO Y USUARIO
    // =====================================================

    List<SeguimientoProyectoResponse>
    listarPorProyectoYUsuario(
            Long proyectoId,
            Long usuarioId
    );

    // =====================================================
    // ÚLTIMOS SEGUIMIENTOS
    // =====================================================

    List<SeguimientoProyectoResponse>
    ultimosSeguimientos();
}