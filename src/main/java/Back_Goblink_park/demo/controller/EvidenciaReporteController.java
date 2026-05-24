package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.EvidenciaReporteRequest;
import Back_Goblink_park.demo.dto.response.EvidenciaReporteResponse;

import Back_Goblink_park.demo.service.interfaces.EvidenciaReporteService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evidencias")
@RequiredArgsConstructor
public class EvidenciaReporteController {

    private final EvidenciaReporteService evidenciaService;

    // =====================================================
    // CREAR EVIDENCIA
    // =====================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EvidenciaReporteResponse crearEvidencia(
            @Valid @RequestBody
            EvidenciaReporteRequest request
    ) {

        return evidenciaService.crearEvidencia(request);
    }

    // =====================================================
    // LISTAR TODAS
    // =====================================================

    @GetMapping
    public List<EvidenciaReporteResponse>
    listarEvidencias() {

        return evidenciaService.listarEvidencias();
    }

    // =====================================================
    // LISTAR POR REPORTE
    // =====================================================

    @GetMapping("/reporte/{reporteId}")
    public List<EvidenciaReporteResponse>
    listarPorReporte(
            @PathVariable Long reporteId
    ) {

        return evidenciaService
                .listarPorReporte(reporteId);
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public EvidenciaReporteResponse obtenerEvidencia(
            @PathVariable Long id
    ) {

        return evidenciaService
                .obtenerEvidencia(id);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarEvidencia(
            @PathVariable Long id
    ) {

        evidenciaService.eliminarEvidencia(id);
    }
}