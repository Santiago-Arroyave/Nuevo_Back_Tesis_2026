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
    // CREAR EVIDENCIA (JSON)
    // =====================================================
    @PostMapping(consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public EvidenciaReporteResponse crearEvidencia(
            @Valid @RequestBody EvidenciaReporteRequest request
    ) {
        return evidenciaService.crearEvidencia(request);
    }

    // =====================================================
    // CREAR EVIDENCIA (MULTIPART/FORM-DATA)
    // =====================================================
    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public EvidenciaReporteResponse crearEvidenciaMultipart(
            @RequestParam("reporteId") Long reporteId,
            @RequestParam(value = "tipoArchivo", defaultValue = "imagen") String tipoArchivo,
            @RequestPart("archivo") org.springframework.web.multipart.MultipartFile archivo,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "esPrincipal", required = false) Boolean esPrincipal
    ) {
        EvidenciaReporteRequest request = EvidenciaReporteRequest.builder()
                .reporteId(reporteId)
                .tipoArchivo(tipoArchivo)
                .archivo(archivo)
                .nombreArchivo(archivo.getOriginalFilename())
                .tamanoArchivo(archivo.getSize())
                .mimeType(archivo.getContentType())
                .descripcion(descripcion)
                .esPrincipal(esPrincipal)
                .build();
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