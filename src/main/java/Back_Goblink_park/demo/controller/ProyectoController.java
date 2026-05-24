package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ProyectoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoResponse;

import Back_Goblink_park.demo.service.interfaces.ProyectoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

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
    // LISTAR TODOS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<ProyectoResponse>>
    listarProyectos() {

        return ResponseEntity.ok(

                proyectoService.listarProyectos()
        );
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponse>
    obtenerProyecto(

            @PathVariable
            Long id
    ) {

        return ResponseEntity.ok(

                proyectoService.obtenerProyecto(id)
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
}