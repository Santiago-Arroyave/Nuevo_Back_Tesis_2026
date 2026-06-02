package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ProyectoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoDetalleResponse;
import Back_Goblink_park.demo.dto.response.ProyectoResponse;
import org.springframework.data.domain.Page;
import Back_Goblink_park.demo.service.interfaces.ProyectoService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import Back_Goblink_park.demo.dto.request.ProyectoCompletoRequest;

import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;



import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    // =====================================================
    // SERVICE
    // =====================================================

    private final ProyectoService proyectoService;

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
// LISTAR PROYECTOS SIN PAGINACIÓN
// =====================================================

    @GetMapping("/todos")
    public ResponseEntity<List<ProyectoResponse>> listarTodosLosProyectos() {

        return ResponseEntity.ok(
                proyectoService.listarProyectos()
        );
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

        return ResponseEntity.ok(

                proyectoService.listarProyectosPaginados(
                        search,
                        estadoProyectoId,
                        prioridadId,
                        page,
                        size
                )
        );
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}/detalle-completo")
    public ResponseEntity<ProyectoDetalleResponse> obtenerDetalleCompleto(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                proyectoService.obtenerDetalleCompleto(id)
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
    // FILTRAR USUARIO
    // =====================================================

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ProyectoResponse>>
    listarPorUsuario(

            @PathVariable
            Long usuarioId
    ) {

        return ResponseEntity.ok(

                proyectoService.listarPorUsuario(
                        usuarioId
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

}