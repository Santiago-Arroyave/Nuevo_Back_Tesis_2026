package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ResponsableProyectoRequest;

import Back_Goblink_park.demo.dto.response.ResponsableProyectoResponse;

import Back_Goblink_park.demo.service.interfaces.ResponsableProyectoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/responsables-proyecto")

@RequiredArgsConstructor

@CrossOrigin("*")
public class ResponsableProyectoController {

    // =====================================================
    // SERVICE
    // =====================================================

    private final ResponsableProyectoService
            responsableService;

    // =====================================================
    // CREAR
    // =====================================================

    @PostMapping
    public ResponseEntity<ResponsableProyectoResponse>
    crearResponsable(

            @RequestBody
            ResponsableProyectoRequest request
    ) {

        return ResponseEntity.status(
                HttpStatus.CREATED
        ).body(

                responsableService
                        .crearResponsable(request)
        );
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    @GetMapping
    public ResponseEntity<
            List<ResponsableProyectoResponse>
            > listarResponsables() {

        return ResponseEntity.ok(

                responsableService
                        .listarResponsables()
        );
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<
            ResponsableProyectoResponse
            > obtenerResponsable(

            @PathVariable Long id
    ) {

        return ResponseEntity.ok(

                responsableService
                        .obtenerResponsable(id)
        );
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<
            ResponsableProyectoResponse
            > actualizarResponsable(

            @PathVariable Long id,

            @RequestBody
            ResponsableProyectoRequest request
    ) {

        return ResponseEntity.ok(

                responsableService
                        .actualizarResponsable(
                                id,
                                request
                        )
        );
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    eliminarResponsable(

            @PathVariable Long id
    ) {

        responsableService
                .eliminarResponsable(id);

        return ResponseEntity.noContent().build();
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<
            List<ResponsableProyectoResponse>
            > listarPorProyecto(

            @PathVariable Long proyectoId
    ) {

        return ResponseEntity.ok(

                responsableService
                        .listarPorProyecto(
                                proyectoId
                        )
        );
    }

    // =====================================================
    // LISTAR POR RESPONSABLE
    // =====================================================

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<
            List<ResponsableProyectoResponse>
            > listarPorUsuario(

            @PathVariable Long usuarioId
    ) {

        return ResponseEntity.ok(

                responsableService
                        .listarPorUsuarioResponsable(
                                usuarioId
                        )
        );
    }

    // =====================================================
    // LISTAR POR ESTADO
    // =====================================================

    @GetMapping("/estado/{estado}")
    public ResponseEntity<
            List<ResponsableProyectoResponse>
            > listarPorEstado(

            @PathVariable String estado
    ) {

        return ResponseEntity.ok(

                responsableService
                        .listarPorEstado(
                                estado
                        )
        );
    }

    // =====================================================
    // LISTAR POR PRIORIDAD
    // =====================================================

    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<
            List<ResponsableProyectoResponse>
            > listarPorPrioridad(

            @PathVariable String prioridad
    ) {

        return ResponseEntity.ok(

                responsableService
                        .listarPorPrioridad(
                                prioridad
                        )
        );
    }



}