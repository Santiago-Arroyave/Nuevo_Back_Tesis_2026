package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ActualizarEstadoReporteRequest;
import Back_Goblink_park.demo.dto.request.ActualizarPrioridadReporteRequest;
import Back_Goblink_park.demo.dto.request.ReporteRequest;
import Back_Goblink_park.demo.dto.request.ReporteUpdateRequest;
import Back_Goblink_park.demo.dto.response.ReporteMapaResponse;
import Back_Goblink_park.demo.dto.response.ReporteResponse;
import Back_Goblink_park.demo.service.interfaces.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    // =====================================================
    // CREAR REPORTE - MULTIPART FORM-DATA (SIMPLIFICADO)
    // =====================================================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ReporteResponse crearReporte(
            // Campos básicos del reporte
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("categoriaId") Long categoriaId,
            @RequestParam("prioridadId") Long prioridadId,
            @RequestParam("latitud") Double latitud,
            @RequestParam("longitud") Double longitud,
            @RequestParam(value = "direccionReferencia", required = false) String direccionReferencia,
            @RequestParam(value = "fuente", required = false, defaultValue = "mobile") String fuente,

            // Archivos de evidencia (pueden ser múltiples)
            @RequestParam(value = "evidencias", required = false) List<MultipartFile> evidencias,

            // Metadatos de evidencias (paralelos a los archivos)
            @RequestParam(value = "evidenciasTipo", required = false) List<String> evidenciasTipo,
            @RequestParam(value = "evidenciasNombre", required = false) List<String> evidenciasNombre,
            @RequestParam(value = "evidenciasDescripcion", required = false) List<String> evidenciasDescripcion,
            @RequestParam(value = "evidenciasPrincipal", required = false) List<Boolean> evidenciasPrincipal
    ) {

        // Construir request básico
        ReporteRequest request = ReporteRequest.builder()
                .titulo(titulo)
                .descripcion(descripcion)
                .categoriaId(categoriaId)
                .prioridadId(prioridadId)
                .latitud(latitud)
                .longitud(longitud)
                .direccionReferencia(direccionReferencia)
                .fuente(fuente)
                .build();

        // Procesar evidencias si vienen archivos
        if (evidencias != null && !evidencias.isEmpty()) {
            List<ReporteRequest.EvidenciaRequest> evidenciasList = new ArrayList<>();

            for (int i = 0; i < evidencias.size(); i++) {
                MultipartFile file = evidencias.get(i);

                // Saltar archivos vacíos
                if (file.isEmpty()) {
                    continue;
                }

                // Obtener metadatos o usar defaults
                String tipo = (evidenciasTipo != null && i < evidenciasTipo.size() && evidenciasTipo.get(i) != null)
                        ? evidenciasTipo.get(i) : "imagen";

                String nombre = (evidenciasNombre != null && i < evidenciasNombre.size() && evidenciasNombre.get(i) != null)
                        ? evidenciasNombre.get(i) : file.getOriginalFilename();

                String desc = (evidenciasDescripcion != null && i < evidenciasDescripcion.size() && evidenciasDescripcion.get(i) != null)
                        ? evidenciasDescripcion.get(i) : "";

                Boolean principal = (evidenciasPrincipal != null && i < evidenciasPrincipal.size() && evidenciasPrincipal.get(i) != null)
                        ? evidenciasPrincipal.get(i) : (i == 0);  // Primera es principal por defecto

                ReporteRequest.EvidenciaRequest evidencia = ReporteRequest.EvidenciaRequest.builder()
                        .archivo(file)
                        .tipoArchivo(tipo)
                        .nombreArchivo(nombre)
                        .tamanoArchivo(file.getSize())
                        .mimeType(file.getContentType())
                        .descripcion(desc)
                        .esPrincipal(principal)
                        .build();

                evidenciasList.add(evidencia);
            }

            request.setEvidencias(evidenciasList);
        }

        return reporteService.crearReporte(request);
    }

    // =====================================================
    // LISTAR REPORTES CON FILTROS Y PAGINACIÓN
    // =====================================================
    @GetMapping
    public ResponseEntity<Page<ReporteResponse>> listarReportes(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long estadoReporteId,
            @RequestParam(required = false) Long prioridadId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                reporteService.listarReportesPaginados(
                        search, categoriaId, estadoReporteId, prioridadId, page, size
                )
        );
    }

    // =====================================================
    // LISTAR REPORTES PARA MAPA
    // =====================================================
    @GetMapping("/mapa")
    public ResponseEntity<List<ReporteMapaResponse>> listarReportesMapa(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long estadoReporteId,
            @RequestParam(required = false) Long prioridadId
    ) {
        return ResponseEntity.ok(
                reporteService.listarReportesMapa(
                        search, categoriaId, estadoReporteId, prioridadId
                )
        );
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================
    @GetMapping("/{id}")
    public ReporteResponse obtenerReporte(@PathVariable Long id) {
        return reporteService.obtenerReporte(id);
    }

    // =====================================================
    // ELIMINAR REPORTE (SOFT DELETE)
    // =====================================================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
    }

    // =====================================================
    // ACTUALIZAR ESTADO
    // =====================================================
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ReporteResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestBody ActualizarEstadoReporteRequest request
    ) {
        return ResponseEntity.ok(reporteService.actualizarEstado(id, request));
    }

    // =====================================================
    // ACTUALIZAR PRIORIDAD
    // =====================================================
    @PatchMapping("/{id}/prioridad")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ReporteResponse> actualizarPrioridad(
            @PathVariable Long id,
            @RequestBody ActualizarPrioridadReporteRequest request
    ) {
        return ResponseEntity.ok(reporteService.actualizarPrioridad(id, request));
    }

    // =====================================================
    // ACTUALIZAR REPORTE COMPLETO - ADMIN O PROPIETARIO
    // =====================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ReporteResponse> actualizarReporte(
            @PathVariable Long id,
            @Valid @RequestBody ReporteUpdateRequest request
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = auth.getName();

        ReporteResponse updated = reporteService.actualizarReporte(id, correoUsuario, request);
        return ResponseEntity.ok(updated);
    }
}