package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.SolicitudProyectoRequest;
import Back_Goblink_park.demo.dto.request.SolicitudResponderRequest;
import Back_Goblink_park.demo.dto.response.SolicitudProyectoResponse;
import Back_Goblink_park.demo.service.interfaces.SolicitudProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-proyecto")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SolicitudProyectoController {

    private final SolicitudProyectoService solicitudService;

    private String getCorreoUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    // =====================================================
    // APP MÓVIL: ENVIAR SOLICITUD
    // =====================================================
    @PostMapping
    public ResponseEntity<SolicitudProyectoResponse> crearSolicitud(
            @RequestBody SolicitudProyectoRequest request) {
        SolicitudProyectoResponse response = solicitudService.crearSolicitud(request, getCorreoUsuario());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =====================================================
    // ADMIN: LISTAR SOLICITUDES PENDIENTES DE UN PROYECTO
    // =====================================================
    @GetMapping("/pendientes/proyecto/{proyectoId}")
    public ResponseEntity<List<SolicitudProyectoResponse>> listarSolicitudesPendientes(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(solicitudService.listarSolicitudesPendientes(proyectoId));
    }

    // =====================================================
    // ADMIN: LISTAR TODAS LAS SOLICITUDES DE UN PROYECTO
    // =====================================================
    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<SolicitudProyectoResponse>> listarSolicitudesPorProyecto(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(solicitudService.listarSolicitudesPorProyecto(proyectoId));
    }

    // =====================================================
    // ADMIN: LISTAR TODAS LAS SOLICITUDES PENDIENTES (DASHBOARD)
    // =====================================================
    @GetMapping("/pendientes")
    public ResponseEntity<List<SolicitudProyectoResponse>> listarTodasPendientes() {
        return ResponseEntity.ok(solicitudService.listarTodasPendientes());
    }

    // =====================================================
    // OBTENER SOLICITUD POR ID
    // =====================================================
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudProyectoResponse> obtenerSolicitud(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.obtenerSolicitud(id));
    }

    // =====================================================
    // ADMIN: ACEPTAR SOLICITUD
    // =====================================================
    @PatchMapping("/{id}/aceptar")
    public ResponseEntity<SolicitudProyectoResponse> aceptarSolicitud(
            @PathVariable Long id,
            @RequestBody SolicitudResponderRequest request) {

        SolicitudProyectoResponse response = solicitudService.aceptarSolicitud(id, request, getCorreoUsuario());
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ADMIN: RECHAZAR SOLICITUD
    // =====================================================
    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<SolicitudProyectoResponse> rechazarSolicitud(
            @PathVariable Long id,
            @RequestBody SolicitudResponderRequest request) {

        SolicitudProyectoResponse response = solicitudService.rechazarSolicitud(id, request, getCorreoUsuario());
        return ResponseEntity.ok(response);
    }
}