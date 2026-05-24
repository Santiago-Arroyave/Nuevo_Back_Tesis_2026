package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.ReporteRequest;
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

    // =====================================================
    // SOFT DELETE
    // =====================================================

    void eliminarReporte(
            Long id
    );
}