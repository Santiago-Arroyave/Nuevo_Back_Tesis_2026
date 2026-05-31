package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.response.dashboard.*;
import Back_Goblink_park.demo.service.interfaces.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DashboardController {

    private final DashboardService dashboardService;

    // =========================================
    // RESUMEN DASHBOARD
    // =========================================

    @GetMapping("/resumen")
    public ResponseEntity<DashboardResumenResponse> obtenerResumen() {

        return ResponseEntity.ok(
                dashboardService.obtenerResumen()
        );
    }

    // =========================================
    // REPORTES POR CATEGORÍA
    // =========================================

    @GetMapping("/reportes-categoria")
    public ResponseEntity<List<DashboardGraficaResponse>>
    obtenerReportesPorCategoria() {

        return ResponseEntity.ok(
                dashboardService.obtenerReportesPorCategoria()
        );
    }

    // =========================================
    // REPORTES POR PRIORIDAD
    // =========================================

    @GetMapping("/reportes-prioridad")
    public ResponseEntity<List<DashboardGraficaResponse>>
    obtenerReportesPorPrioridad() {

        return ResponseEntity.ok(
                dashboardService.obtenerReportesPorPrioridad()
        );
    }

    // =====================================================
// ACTIVIDADES VENCIDAS
// =====================================================
    @GetMapping("/actividades-vencidas")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public List<ActividadVencidaDTO> obtenerActividadesVencidas() {
        return dashboardService.obtenerActividadesVencidas();
    }

    // =====================================================
// ACTIVIDAD RECIENTE (FEED)
// =====================================================
    @GetMapping("/actividad-reciente")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public List<ActividadRecienteDTO> obtenerActividadReciente() {
        return dashboardService.obtenerActividadReciente();
    }

    // =====================================================
// EVOLUCIÓN DE REPORTES (GRÁFICA LÍNEA)
// =====================================================
    @GetMapping("/evolucion-reportes")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public List<EvolucionReporteDTO> obtenerEvolucionReportes(
            @RequestParam(defaultValue = "30") Integer dias
    ) {
        return dashboardService.obtenerEvolucionReportes(dias);
    }
}