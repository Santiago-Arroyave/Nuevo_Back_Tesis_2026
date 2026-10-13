package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ProyectoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoDetalleResponse;
import Back_Goblink_park.demo.dto.response.ProyectoResponse;
import Back_Goblink_park.demo.service.impl.ProyectoPdfService;
import org.springframework.data.domain.Page;
import Back_Goblink_park.demo.service.interfaces.ProyectoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import Back_Goblink_park.demo.dto.request.ProyectoCompletoRequest;
import Back_Goblink_park.demo.dto.response.CronogramaActividadProyectoResponse;
import Back_Goblink_park.demo.service.interfaces.CronogramaActividadProyectoService;

import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    // =====================================================
    // SERVICE
    // =====================================================

    private final ProyectoService proyectoService;
    private final ProyectoPdfService proyectoPdfService;
    private final CronogramaActividadProyectoService actividadService;

    // =====================================================
    // CREAR
    // =====================================================

    // =====================================================
// ACTUALIZAR ESTADO DEL PROYECTO (Planificación, En ejecución, etc.)
// =====================================================
    @PatchMapping("/{id}/estado-proyecto")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProyectoResponse> actualizarEstadoProyecto(
            @PathVariable Long id,
            @RequestParam Long estadoProyectoId
    ) {
        ProyectoResponse updated = proyectoService.actualizarEstadoProyecto(id, estadoProyectoId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public ResponseEntity<ProyectoResponse> crearProyecto(

            @RequestBody
            ProyectoRequest request,

            Authentication authentication
    ) {

        String correoUsuario =
                authentication.getName();

        ProyectoResponse response =
                proyectoService.crearProyecto(
                        request,
                        correoUsuario
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =====================================================
// CREAR PROYECTO COMPLETO
// =====================================================

    @PostMapping("/completo")
    public ResponseEntity<ProyectoDetalleResponse> crearProyectoCompleto(
            @RequestBody ProyectoCompletoRequest request
    ) {

        // =================================================
        // USUARIO AUTENTICADO
        // =================================================

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String correoUsuario =
                authentication.getName();

        // =================================================
        // CREAR PROYECTO COMPLETO
        // =================================================

        ProyectoDetalleResponse response =
                proyectoService.crearProyectoCompleto(
                        request,
                        correoUsuario
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =====================================================
// CONVERTIR REPORTES EN PROYECTO
// =====================================================

    @PostMapping("/convertir-reportes")
    public ResponseEntity<ProyectoDetalleResponse> convertirReportesEnProyecto(

            @Valid
            @RequestBody ProyectoCompletoRequest request
    ) {

        // =================================================
        // USUARIO AUTENTICADO
        // =================================================

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String correoUsuario =
                authentication.getName();

        // =================================================
        // CONVERTIR REPORTES EN PROYECTO
        // =================================================

        ProyectoDetalleResponse response =
                proyectoService.convertirReportesEnProyecto(
                        request,
                        correoUsuario
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =====================================================
// ACTUALIZAR PROYECTO COMPLETO
// =====================================================

    @PutMapping("/{id}/completo")
    public ResponseEntity<ProyectoDetalleResponse> actualizarProyectoCompleto(

            @PathVariable Long id,

            @Valid
            @RequestBody ProyectoCompletoRequest request
    ) {

        return ResponseEntity.ok(

                proyectoService.actualizarProyectoCompleto(
                        id,
                        request
                )
        );
    }

    // =====================================================
    // MIS PROYECTOS (USUARIO AUTENTICADO)
    // =====================================================

    @GetMapping("/mis-proyectos")
    public ResponseEntity<List<ProyectoResponse>> listarMisProyectos(
            Authentication authentication
    ) {
        long start = System.currentTimeMillis();
        String correoUsuario = authentication.getName();
        log.info("[ProyectoController] GET /api/proyectos/mis-proyectos - Petición recibida para usuario: {}", correoUsuario);

        List<ProyectoResponse> response = proyectoService.listarMisProyectos(correoUsuario);

        log.info("[ProyectoController] GET /api/proyectos/mis-proyectos - Respondido en {} ms (elementos={})",
                (System.currentTimeMillis() - start), response.size());
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // PROYECTOS POR ID DE USUARIO (MÓVIL / EXPLORACIÓN)
    // =====================================================

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ProyectoResponse>> listarProyectosPorUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(required = false, defaultValue = "false") boolean soloCreados
    ) {
        long start = System.currentTimeMillis();
        log.info("[ProyectoController] GET /api/proyectos/usuario/{} - Petición recibida (soloCreados={})", usuarioId, soloCreados);

        List<ProyectoResponse> response = soloCreados
                ? proyectoService.listarPorUsuario(usuarioId)
                : proyectoService.listarProyectosDondeParticipaUsuario(usuarioId);

        log.info("[ProyectoController] GET /api/proyectos/usuario/{} - Respondido en {} ms (elementos={})",
                usuarioId, (System.currentTimeMillis() - start), response.size());
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // EXPLORAR PROYECTOS (USUARIO AUTENTICADO)
    // =====================================================

    @GetMapping("/explorar")
    public ResponseEntity<List<ProyectoResponse>> explorarProyectos(
            @RequestParam(required = false) String categoria,
            Authentication authentication
    ) {
        long start = System.currentTimeMillis();
        String correoUsuario = authentication != null ? authentication.getName() : null;
        log.info("[ProyectoController] GET /api/proyectos/explorar - Petición recibida para: {}, categoria: {}", correoUsuario, categoria);

        List<ProyectoResponse> response = proyectoService.explorarProyectos(correoUsuario, categoria);

        log.info("[ProyectoController] GET /api/proyectos/explorar - Respondido en {} ms (elementos={})",
                (System.currentTimeMillis() - start), response.size());
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // LISTAR PROYECTOS SIN PAGINACIÓN
    // =====================================================

    @GetMapping("/todos")
    public ResponseEntity<List<ProyectoResponse>> listarTodosLosProyectos() {
        long start = System.currentTimeMillis();
        log.info("[ProyectoController] GET /api/proyectos/todos - Petición recibida");

        List<ProyectoResponse> response = proyectoService.listarProyectos();

        log.info("[ProyectoController] GET /api/proyectos/todos - Respondido en {} ms (elementos={})",
                (System.currentTimeMillis() - start), response.size());
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // LISTAR PROYECTOS CON FILTROS Y PAGINACIÓN
    // =====================================================

    @GetMapping
    public ResponseEntity<Page<ProyectoResponse>> listarProyectos(

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            Long estadoProyectoId,

            @RequestParam(required = false)
            Long prioridadId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {
        long start = System.currentTimeMillis();
        log.info("[ProyectoController] GET /api/proyectos - Petición recibida (page={}, size={}, search={})", page, size, search);

        Page<ProyectoResponse> response = proyectoService.listarProyectosPaginados(
                search,
                estadoProyectoId,
                prioridadId,
                page,
                size
        );

        log.info("[ProyectoController] GET /api/proyectos - Respondido en {} ms (totalElementos={}, pagina={})",
                (System.currentTimeMillis() - start), response.getTotalElements(), response.getNumber());
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // OBTENER POR ID / RESUMEN COMPLETO
    // =====================================================

    @GetMapping(value = {"/{id}/detalle-completo", "/{id}/resumen-completo"})
    public ResponseEntity<ProyectoDetalleResponse> obtenerDetalleCompleto(
            @PathVariable Long id
    ) {
        long start = System.currentTimeMillis();
        log.info("[ProyectoController] GET /api/proyectos/{}/detalle-completo - Petición recibida", id);

        ProyectoDetalleResponse response = proyectoService.obtenerDetalleCompleto(id);

        log.info("[ProyectoController] GET /api/proyectos/{}/detalle-completo - Respondido en {} ms",
                id, (System.currentTimeMillis() - start));
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ACTIVIDADES CRONOLÓGICAS DE UN PROYECTO
    // =====================================================

    @GetMapping("/{id}/actividades")
    public ResponseEntity<List<CronogramaActividadProyectoResponse>> listarActividadesProyecto(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                actividadService.listarActividadesPorProyectoCronologico(id)
        );
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoResponse>
    actualizarProyecto(

            @PathVariable
            Long id,

            @RequestBody
            ProyectoRequest request
    ) {

        return ResponseEntity.ok(

                proyectoService.actualizarProyecto(
                        id,
                        request
                )
        );
    }

    // =====================================================
    // ELIMINAR LÓGICO
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProyecto(

            @PathVariable
            Long id
    ) {

        proyectoService.eliminarProyecto(id);

        return ResponseEntity.noContent().build();
    }

    // =====================================================
    // FILTRAR ESTADO PROYECTO
    // =====================================================

    @GetMapping("/estado/{estadoProyectoId}")
    public ResponseEntity<List<ProyectoResponse>>
    listarPorEstadoProyecto(

            @PathVariable
            Long estadoProyectoId
    ) {

        return ResponseEntity.ok(

                proyectoService.listarPorEstadoProyecto(
                        estadoProyectoId
                )
        );
    }

    // =====================================================
    // FILTRAR PRIORIDAD
    // =====================================================

    @GetMapping("/prioridad/{prioridadId}")
    public ResponseEntity<List<ProyectoResponse>>
    listarPorPrioridad(

            @PathVariable
            Long prioridadId
    ) {

        return ResponseEntity.ok(

                proyectoService.listarPorPrioridad(
                        prioridadId
                )
        );
    }

    // =====================================================
    // FILTRAR CATEGORÍA
    // =====================================================

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProyectoResponse>>
    listarPorCategoria(

            @PathVariable
            Long categoriaId
    ) {

        return ResponseEntity.ok(

                proyectoService.listarPorCategoria(
                        categoriaId
                )
        );
    }

    // =====================================================
    // BUSCAR NOMBRE
    // =====================================================

    @GetMapping("/buscar")
    public ResponseEntity<List<ProyectoResponse>>
    buscarPorNombre(

            @RequestParam
            String nombre
    ) {

        return ResponseEntity.ok(

                proyectoService.buscarPorNombre(
                        nombre
                )
        );
    }
    // En ProyectoController.java

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponse> obtenerProyecto(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.obtenerProyecto(id));
    }
    // =====================================================
// CAMBIAR ESTADO DEL PROYECTO - SOLO ADMIN
// =====================================================
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProyectoResponse> cambiarEstadoProyecto(
            @PathVariable Long id,
            @RequestParam Boolean estado
    ) {
        ProyectoResponse updated = proyectoService.cambiarEstadoProyecto(id, estado);
        return ResponseEntity.ok(updated);
    }

    // =====================================================
// DESCARGAR PROYECTO EN PDF
// =====================================================

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarProyectoPdf(
            @PathVariable Long id
    ) {
        try {
            // Obtener detalle completo del proyecto
            ProyectoDetalleResponse proyecto =
                    proyectoService.obtenerDetalleCompleto(id);

            // Generar PDF editorial
            byte[] pdfContent = proyectoPdfService.generarPdfProyecto(proyecto);

            if (pdfContent == null) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }

            String rawNombre = (proyecto.getProyecto() != null && proyecto.getProyecto().getNombre() != null)
                    ? proyecto.getProyecto().getNombre()
                    : "Proyecto-" + id;
            String slug = java.text.Normalizer.normalize(rawNombre, java.text.Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .replaceAll("[^a-zA-Z0-9\\s-]", "")
                    .trim()
                    .replaceAll("\\s+", "-");
            String fileName = slug.isEmpty() ? ("Ficha-Proyecto-" + id + ".pdf") : ("Ficha-" + slug + ".pdf");

            // Configurar headers para descarga
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData(
                    "attachment",
                    fileName
            );
            headers.setContentLength(pdfContent.length);

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}