package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ActualizarEstadoReporteRequest;
import Back_Goblink_park.demo.dto.request.ActualizarPrioridadReporteRequest;
import Back_Goblink_park.demo.dto.request.ReporteRequest;
import Back_Goblink_park.demo.dto.response.ProyectoResponse;
import Back_Goblink_park.demo.dto.response.ReporteResponse;
import Back_Goblink_park.demo.service.interfaces.ProyectoService;
import Back_Goblink_park.demo.service.interfaces.ReporteService;
import org.springframework.data.domain.Page;
import Back_Goblink_park.demo.dto.response.ReporteMapaResponse;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;
    private final ProyectoService proyectoService;
    // =====================================================
    // CREAR REPORTE
    // =====================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReporteResponse crearReporte(
            @Valid @RequestBody ReporteRequest request
    ) {

        return reporteService.crearReporte(request);
    }

    // =====================================================
    // LISTAR REPORTES
    // =====================================================

    // =====================================================
// LISTAR REPORTES CON FILTROS Y PAGINACIÓN
// =====================================================

    @GetMapping
    public ResponseEntity<Page<ReporteResponse>> listarReportes(

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            Long categoriaId,

            @RequestParam(required = false)
            Long estadoReporteId,

            @RequestParam(required = false)
            Long prioridadId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return ResponseEntity.ok(

                reporteService.listarReportesPaginados(
                        search,
                        categoriaId,
                        estadoReporteId,
                        prioridadId,
                        page,
                        size
                )
        );
    }


    // =====================================================
// LISTAR REPORTES PARA MAPA
// =====================================================

    @GetMapping("/mapa")
    public ResponseEntity<List<ReporteMapaResponse>> listarReportesMapa(

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            Long categoriaId,

            @RequestParam(required = false)
            Long estadoReporteId,

            @RequestParam(required = false)
            Long prioridadId
    ) {

        return ResponseEntity.ok(

                reporteService.listarReportesMapa(
                        search,
                        categoriaId,
                        estadoReporteId,
                        prioridadId
                )
        );
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ReporteResponse obtenerReporte(
            @PathVariable Long id
    ) {

        return reporteService.obtenerReporte(id);
    }

    // =====================================================
    // ELIMINAR REPORTE
    // =====================================================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarReporte(
            @PathVariable Long id
    ) {

        reporteService.eliminarReporte(id);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ReporteResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestBody ActualizarEstadoReporteRequest request
    ) {

        return ResponseEntity.ok(
                reporteService.actualizarEstado(
                        id,
                        request
                )
        );
    }

    // =====================================================
// ACTUALIZAR PRIORIDAD
// =====================================================

    @PatchMapping("/{id}/prioridad")
    public ResponseEntity<ReporteResponse> actualizarPrioridad(
            @PathVariable Long id,
            @RequestBody ActualizarPrioridadReporteRequest request
    ) {

        return ResponseEntity.ok(
                reporteService.actualizarPrioridad(
                        id,
                        request
                )
        );
    }



}