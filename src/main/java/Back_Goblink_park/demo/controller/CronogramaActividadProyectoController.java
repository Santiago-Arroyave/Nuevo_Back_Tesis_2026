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
    // MARCAR ACTIVIDAD COMO COMPLETADA (CON IMAGEN → BASE64)
    // =====================================================

    @PatchMapping(
            value = "/{id}/completar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CronogramaActividadProyectoResponse> marcarActividadCompletada(
            @PathVariable Long id,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "descripcionEvidencia", required = false) String descripcionEvidencia,
            @RequestPart(value = "observaciones", required = false) String observaciones) {

        CronogramaActividadCompletarRequest request = CronogramaActividadCompletarRequest.builder()
                .descripcionEvidencia(descripcionEvidencia)
                .observaciones(observaciones)
                .build();

        // ✅ SI HAY ARCHIVO, CONVERTIRLO A BASE64
        if (file != null && !file.isEmpty()) {
            try {
                // Convertir archivo a bytes
                byte[] bytes = file.getBytes();

                // ✅ CODIFICAR A BASE64
                String base64String = Base64.getEncoder().encodeToString(bytes);

                // Guardar en el request
                request.setImagenBase64(base64String);

                // Guardar tipo de imagen (image/jpeg, image/png, etc.)
                request.setTipoImagen(file.getContentType());

                System.out.println("✅ Imagen convertida a Base64 exitosamente");
                System.out.println("Tipo: " + file.getContentType());
                System.out.println("Tamaño Base64: " + base64String.length() + " caracteres");

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        // Llamar al service con el request completo
        CronogramaActividadProyectoResponse response = actividadService.marcarActividadCompletadaConEvidencia(id, request);
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