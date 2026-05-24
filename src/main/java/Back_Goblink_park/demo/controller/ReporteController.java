package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ReporteRequest;
import Back_Goblink_park.demo.dto.response.ReporteResponse;
import Back_Goblink_park.demo.service.interfaces.ReporteService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

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

    @GetMapping
    public List<ReporteResponse> listarReportes() {

        return reporteService.listarReportes();
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
}