package Back_Goblink_park.demo.service.interfaces;

import Back_Goblink_park.demo.dto.response.dashboard.*;

import java.util.List;

public interface DashboardService {

    // =========================================
    // RESUMEN DASHBOARD
    // =========================================

    DashboardResumenResponse obtenerResumen();

    // =========================================
    // REPORTES POR CATEGORÍA
    // =========================================

    List<DashboardGraficaResponse> obtenerReportesPorCategoria();

    // =========================================
    // REPORTES POR PRIORIDAD
    // =========================================

    List<DashboardGraficaResponse> obtenerReportesPorPrioridad();

    List<ActividadVencidaDTO> obtenerActividadesVencidas();

    List<ActividadRecienteDTO> obtenerActividadReciente();

    List<EvolucionReporteDTO> obtenerEvolucionReportes(Integer dias);
}