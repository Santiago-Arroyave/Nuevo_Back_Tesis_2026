package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.SeguimientoProyectoRequest;

import Back_Goblink_park.demo.dto.response.SeguimientoProyectoResponse;

import Back_Goblink_park.demo.service.interfaces.SeguimientoProyectoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguimientos-proyecto")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SeguimientoProyectoController {

    // =====================================================
    // SERVICE
    // =====================================================

    private final SeguimientoProyectoService
            seguimientoService;

    // =====================================================
    // CREAR
    // =====================================================

    @PostMapping
    public ResponseEntity<SeguimientoProyectoResponse>
    crearSeguimiento(

            @RequestBody
            SeguimientoProyectoRequest request
    ) {

        return ResponseEntity.status(
                HttpStatus.CREATED
        ).body(

                seguimientoService
                        .crearSeguimiento(request)
        );
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    @GetMapping
    public ResponseEntity<
            List<SeguimientoProyectoResponse>
            > listarSeguimientos() {

        return ResponseEntity.ok(

                seguimientoService
                        .listarSeguimientos()
        );
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<
            SeguimientoProyectoResponse
            > obtenerSeguimiento(

            @PathVariable Long id
    ) {

        return ResponseEntity.ok(

                seguimientoService
                        .obtenerSeguimiento(id)
        );
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<
            SeguimientoProyectoResponse
            > actualizarSeguimiento(

            @PathVariable Long id,

            @RequestBody
            SeguimientoProyectoRequest request
    ) {

        return ResponseEntity.ok(

                seguimientoService
                        .actualizarSeguimiento(
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
    eliminarSeguimiento(

            @PathVariable Long id
    ) {

        seguimientoService
                .eliminarSeguimiento(id);

        return ResponseEntity.noContent()
                .build();
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<
            List<SeguimientoProyectoResponse>
            > listarPorProyecto(

            @PathVariable Long proyectoId
    ) {

        return ResponseEntity.ok(

                seguimientoService
                        .listarPorProyecto(
                                proyectoId
                        )
        );
    }

    // =====================================================
    // LISTAR POR USUARIO
    // =====================================================

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<
            List<SeguimientoProyectoResponse>
            > listarPorUsuario(

            @PathVariable Long usuarioId
    ) {

        return ResponseEntity.ok(

                seguimientoService
                        .listarPorUsuario(
                                usuarioId
                        )
        );
    }

    // =====================================================
    // LISTAR POR PROYECTO Y USUARIO
    // =====================================================

    @GetMapping(
            "/proyecto/{proyectoId}/usuario/{usuarioId}"
    )
    public ResponseEntity<
            List<SeguimientoProyectoResponse>
            > listarPorProyectoYUsuario(

            @PathVariable Long proyectoId,

            @PathVariable Long usuarioId
    ) {

        return ResponseEntity.ok(

                seguimientoService
                        .listarPorProyectoYUsuario(
                                proyectoId,
                                usuarioId
                        )
        );
    }

    // =====================================================
    // ÚLTIMOS SEGUIMIENTOS
    // =====================================================

    @GetMapping("/ultimos")
    public ResponseEntity<
            List<SeguimientoProyectoResponse>
            > ultimosSeguimientos() {

        return ResponseEntity.ok(

                seguimientoService
                        .ultimosSeguimientos()
        );
    }
}