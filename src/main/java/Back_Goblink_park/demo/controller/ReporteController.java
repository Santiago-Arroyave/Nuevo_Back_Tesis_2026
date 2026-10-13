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
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    // =====================================================
    // CREAR REPORTE - MULTIPART FORM-DATA (CON IDEMPOTENCIA OFFLINE)
    // =====================================================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReporteResponse> crearReporte(
            // Campos básicos del reporte
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("categoriaId") Long categoriaId,
            @RequestParam("prioridadId") Long prioridadId,
            @RequestParam("latitud") Double latitud,
            @RequestParam("longitud") Double longitud,
            @RequestParam(value = "direccionReferencia", required = false) String direccionReferencia,
            @RequestParam(value = "fuente", required = false, defaultValue = "mobile") String fuente,
            @RequestParam(value = "sector", required = false) String sector,
            @RequestParam(value = "datosEspecificos", required = false) String datosEspecificos,
            @RequestParam(value = "datos_especificos", required = false) String datosEspecificosSnake,
            @RequestParam(value = "clientUuid", required = false) String clientUuid,
            @RequestParam(value = "client_uuid", required = false) String clientUuidSnake,
            @RequestHeader(value = "X-Client-UUID", required = false) String headerClientUuid,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,

            // Archivos de evidencia (pueden ser múltiples)
            @RequestParam(value = "evidencias", required = false) List<MultipartFile> evidencias,

            // Metadatos de evidencias (paralelos a los archivos)
            @RequestParam(value = "evidenciasTipo", required = false) List<String> evidenciasTipo,
            @RequestParam(value = "evidenciasNombre", required = false) List<String> evidenciasNombre,
            @RequestParam(value = "evidenciasDescripcion", required = false) List<String> evidenciasDescripcion,
            @RequestParam(value = "evidenciasPrincipal", required = false) List<Boolean> evidenciasPrincipal
    ) {
        String resolvedClientUuid = resolverClientUuid(clientUuid, clientUuidSnake, headerClientUuid, idempotencyKey);

        // Deserializar datosEspecificos si vienen como cadena JSON (multipart)
        Map<String, Object> mapDatos = null;
        String rawJson = (datosEspecificos != null && !datosEspecificos.isBlank())
                ? datosEspecificos
                : datosEspecificosSnake;

        if (rawJson != null && !rawJson.isBlank()) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                mapDatos = objectMapper.readValue(rawJson, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
            } catch (Exception e) {
                // Loguear o ignorar si no es un JSON válido
            }
        }

        // Construir request básico
        ReporteRequest request = ReporteRequest.builder()
                .clientUuid(resolvedClientUuid)
                .titulo(titulo)
                .descripcion(descripcion)
                .categoriaId(categoriaId)
                .prioridadId(prioridadId)
                .latitud(latitud)
                .longitud(longitud)
                .direccionReferencia(direccionReferencia)
                .fuente(fuente)
                .sector(sector)
                .datosEspecificos(mapDatos)
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

        return procesarCreacionConIdempotencia(request);
    }

    // =====================================================
    // CREAR REPORTE - APPLICATION/JSON (CON IDEMPOTENCIA OFFLINE)
    // =====================================================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReporteResponse> crearReporteJson(
            @Valid @RequestBody ReporteRequest request,
            @RequestHeader(value = "X-Client-UUID", required = false) String headerClientUuid,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ) {
        String resolved = resolverClientUuid(request.getClientUuid(), null, headerClientUuid, idempotencyKey);
        request.setClientUuid(resolved);
        return procesarCreacionConIdempotencia(request);
    }

    private ResponseEntity<ReporteResponse> procesarCreacionConIdempotencia(ReporteRequest request) {
        try {
            ReporteResponse response = reporteService.crearReporte(request);
            if (response != null && response.isIdempotentExistente()) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            if (request.getClientUuid() != null && !request.getClientUuid().isBlank()) {
                ReporteResponse existente = reporteService.buscarPorClientUuid(request.getClientUuid());
                if (existente != null) {
                    return ResponseEntity.ok(existente);
                }
            }
            throw ex;
        }
    }

    private String resolverClientUuid(String... candidatos) {
        if (candidatos == null) return null;
        for (String c : candidatos) {
            if (c != null && !c.isBlank()) {
                return c.trim();
            }
        }
        return null;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReporteResponse> actualizarReporte(
            @PathVariable Long id,
            @Valid @RequestBody ReporteUpdateRequest request,
            Authentication authentication
    ) {
        String correoUsuario = authentication.getName();
        ReporteResponse updated = reporteService.actualizarReporte(id, correoUsuario, request);
        return ResponseEntity.ok(updated);
    }
}