package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.CronogramaActividadCompletarRequest;
import Back_Goblink_park.demo.dto.request.CronogramaActividadProyectoRequest;
import Back_Goblink_park.demo.dto.response.CronogramaActividadProyectoResponse;
import Back_Goblink_park.demo.service.interfaces.CronogramaActividadProyectoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Base64;  // ← ✅ IMPORT PARA BASE64
import java.util.List;

@RestController
@RequestMapping("/api/cronograma-actividades")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CronogramaActividadProyectoController {

    private final CronogramaActividadProyectoService actividadService;

    // =====================================================
    // CREAR
    // =====================================================

    @PostMapping
    public ResponseEntity<CronogramaActividadProyectoResponse> crearActividad(
            @RequestBody CronogramaActividadProyectoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(actividadService.crearActividad(request));
    }

    // =====================================================
    // LISTAR TODAS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<CronogramaActividadProyectoResponse>> listarActividades() {
        return ResponseEntity.ok(actividadService.listarActividades());
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<CronogramaActividadProyectoResponse> obtenerActividad(@PathVariable Long id) {
        return ResponseEntity.ok(actividadService.obtenerActividad(id));
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<CronogramaActividadProyectoResponse>> listarPorProyecto(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(actividadService.listarPorProyecto(proyectoId));
    }

    // =====================================================
    // LISTAR POR MIEMBRO DEL PROYECTO
    // =====================================================

    @GetMapping("/miembro/{proyectoMiembroId}")
    public ResponseEntity<List<CronogramaActividadProyectoResponse>> listarPorMiembro(@PathVariable Long proyectoMiembroId) {
        return ResponseEntity.ok(actividadService.listarPorMiembro(proyectoMiembroId));
    }

    // =====================================================
    // LISTAR POR ESTADO
    // =====================================================

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CronogramaActividadProyectoResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(actividadService.listarPorEstado(estado));
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<CronogramaActividadProyectoResponse> actualizarActividad(
            @PathVariable Long id, @RequestBody CronogramaActividadProyectoRequest request) {
        return ResponseEntity.ok(actividadService.actualizarActividad(id, request));
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarActividad(@PathVariable Long id) {
        actividadService.eliminarActividad(id);
        return ResponseEntity.noContent().build();
    }

    // =====================================================
    // MARCAR ACTIVIDAD COMO COMPLETADA (CON MÚLTIPLES IMÁGENES → BASE64)
    // =====================================================

    @PatchMapping(
            value = "/{id}/completar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CronogramaActividadProyectoResponse> marcarActividadCompletada(
            @PathVariable Long id,
            @RequestPart(value = "files", required = false) List<MultipartFile> files, // ✅ CAMBIADO A LISTA
            @RequestPart(value = "descripcionEvidencia", required = false) String descripcionEvidencia,
            @RequestPart(value = "observaciones", required = false) String observaciones) {

        // ✅ AGREGA ESTOS LOGS PARA DEPURAR
        System.out.println("🔍 [CONTROLLER] ID Actividad: " + id);
        System.out.println("🔍 [CONTROLLER] Cantidad de archivos recibidos: " + (files != null ? files.size() : "NULL"));
        System.out.println("🔍 [CONTROLLER] Observaciones: " + observaciones);

        // Llamar al service pasando la lista de archivos directamente
        CronogramaActividadProyectoResponse response = actividadService.marcarActividadCompletadaConEvidencia(
                id, files, descripcionEvidencia, observaciones
        );

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // RECALCULAR AVANCE DEL PROYECTO
    // =====================================================

    @PatchMapping("/proyecto/{proyectoId}/recalcular-avance")
    public ResponseEntity<BigDecimal> recalcularAvanceProyecto(@PathVariable Long proyectoId) {
        BigDecimal avance = actividadService.recalcularAvanceProyecto(proyectoId);
        return ResponseEntity.ok(avance);
    }
}