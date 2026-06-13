package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ActualizarEstadoReporteRequest;
import Back_Goblink_park.demo.dto.request.ActualizarPrioridadReporteRequest;
import Back_Goblink_park.demo.dto.request.ReporteRequest;
import Back_Goblink_park.demo.dto.request.ReporteUpdateRequest;
import Back_Goblink_park.demo.dto.response.ReporteMapaResponse;
import org.springframework.data.domain.Page;
import Back_Goblink_park.demo.dto.response.ReporteResponse;

import java.util.List;

public interface ReporteService {

    // =====================================================
    // CREAR
    // =====================================================

    ReporteResponse crearReporte(
            ReporteRequest request
    );

    // =====================================================
    // LISTAR
    // =====================================================

    List<ReporteResponse> listarReportes();

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    ReporteResponse obtenerReporte(
            Long id
    );

    // =====================================================
    // FILTROS
    // =====================================================

    List<ReporteResponse> listarPorCategoria(
            Long categoriaId
    );

    List<ReporteResponse> listarPorPrioridad(
            Long prioridadId
    );

    List<ReporteResponse> listarPorEstado(
            Long estadoReporteId
    );

    ReporteResponse actualizarEstado(
            Long reporteId,
            ActualizarEstadoReporteRequest request
    );

    // =====================================================
// ACTUALIZAR PRIORIDAD
// =====================================================

    ReporteResponse actualizarPrioridad(
            Long reporteId,
            ActualizarPrioridadReporteRequest request
    );

    // ACTUALIZAR REPORTE
    ReporteResponse actualizarReporte(Long id, String correoUsuario, ReporteUpdateRequest request);
    // =====================================================
    // SOFT DELETE
    // =====================================================

    void eliminarReporte(
            Long id
    );

    // =====================================================
// LISTAR REPORTES CON FILTROS Y PAGINACIÓN
// =====================================================

    Page<ReporteResponse> listarReportesPaginados(
            String search,
            Long categoriaId,
            Long estadoReporteId,
            Long prioridadId,
            int page,
            int size
    );
    // =====================================================
// LISTAR REPORTES PARA MAPA
// =====================================================

    List<ReporteMapaResponse> listarReportesMapa(
            String search,
            Long categoriaId,
            Long estadoReporteId,
            Long prioridadId
    );


}