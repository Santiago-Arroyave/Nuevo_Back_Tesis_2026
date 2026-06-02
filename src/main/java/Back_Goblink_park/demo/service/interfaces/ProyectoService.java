package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ProyectoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoResponse;
import Back_Goblink_park.demo.dto.response.ProyectoDetalleResponse;
import Back_Goblink_park.demo.dto.request.ProyectoCompletoRequest;



import org.springframework.data.domain.Page;

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

    // =====================================================
// LISTAR PROYECTOS CON FILTROS Y PAGINACIÓN
// =====================================================

    Page<ProyectoResponse> listarProyectosPaginados(
            String search,
            Long estadoProyectoId,
            Long prioridadId,
            int page,
            int size
    );
    // =====================================================
// OBTENER DETALLE COMPLETO DEL PROYECTO
// =====================================================

    ProyectoDetalleResponse obtenerDetalleCompleto(
            Long proyectoId
    );
    // =====================================================
// CREAR PROYECTO COMPLETO
// =====================================================

    ProyectoDetalleResponse crearProyectoCompleto(
            ProyectoCompletoRequest request,
            String correoUsuario
    );

    // =====================================================
// ACTUALIZAR PROYECTO COMPLETO
// =====================================================

    ProyectoDetalleResponse actualizarProyectoCompleto(
            Long proyectoId,
            ProyectoCompletoRequest request
    );
    // =====================================================
// CONVERTIR REPORTES EN PROYECTO
// =====================================================

    ProyectoDetalleResponse convertirReportesEnProyecto(
            ProyectoCompletoRequest request,
            String correoUsuario
    );

    // En ProyectoService.java
    ProyectoResponse cambiarEstadoProyecto(Long id, Boolean estado);

    // Método para cambiar el estado del proyecto (Planificación, En ejecución, etc.)
    ProyectoResponse actualizarEstadoProyecto(Long id, Long estadoProyectoId);

}