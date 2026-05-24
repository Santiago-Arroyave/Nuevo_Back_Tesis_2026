package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.request.EvidenciaReporteRequest;
import Back_Goblink_park.demo.dto.response.EvidenciaReporteResponse;

import java.util.List;

public interface EvidenciaReporteService {

    // =====================================================
    // CREAR EVIDENCIA
    // =====================================================

    EvidenciaReporteResponse crearEvidencia(
            EvidenciaReporteRequest request
    );

    // =====================================================
    // LISTAR TODAS
    // =====================================================

    List<EvidenciaReporteResponse> listarEvidencias();

    // =====================================================
    // LISTAR POR REPORTE
    // =====================================================

    List<EvidenciaReporteResponse> listarPorReporte(
            Long reporteId
    );

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    EvidenciaReporteResponse obtenerEvidencia(
            Long id
    );

    // =====================================================
    // ELIMINAR
    // =====================================================

    void eliminarEvidencia(
            Long id
    );
}