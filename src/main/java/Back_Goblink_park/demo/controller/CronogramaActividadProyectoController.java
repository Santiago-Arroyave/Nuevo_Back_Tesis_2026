package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.CronogramaActividadProyectoRequest;

import Back_Goblink_park.demo.dto.response.CronogramaActividadProyectoResponse;

import Back_Goblink_park.demo.service.interfaces.CronogramaActividadProyectoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        "/api/cronograma-actividades"
)
@RequiredArgsConstructor
@CrossOrigin("*")
public class CronogramaActividadProyectoController {

    // =====================================================
    // SERVICE
    // =====================================================

    private final CronogramaActividadProyectoService
            actividadService;

    // =====================================================
    // CREAR
    // =====================================================

    @PostMapping
    public ResponseEntity<
            CronogramaActividadProyectoResponse
            > crearActividad(

            @RequestBody
            CronogramaActividadProyectoRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)

                .body(

                        actividadService
                                .crearActividad(request)
                );
    }

    // =====================================================
    // LISTAR TODAS
    // =====================================================

    @GetMapping
    public ResponseEntity<
            List<CronogramaActividadProyectoResponse>
            > listarActividades() {

        return ResponseEntity.ok(

                actividadService
                        .listarActividades()
        );
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<
            CronogramaActividadProyectoResponse
            > obtenerActividad(

            @PathVariable Long id
    ) {

        return ResponseEntity.ok(

                actividadService
                        .obtenerActividad(id)
        );
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<
            List<CronogramaActividadProyectoResponse>
            > listarPorProyecto(

            @PathVariable Long proyectoId
    ) {

        return ResponseEntity.ok(

                actividadService
                        .listarPorProyecto(proyectoId)
        );
    }

    // =====================================================
    // LISTAR POR RESPONSABLE
    // =====================================================

    @GetMapping("/responsable/{responsableId}")
    public ResponseEntity<
            List<CronogramaActividadProyectoResponse>
            > listarPorResponsable(

            @PathVariable Long responsableId
    ) {

        return ResponseEntity.ok(

                actividadService
                        .listarPorResponsable(
                                responsableId
                        )
        );
    }

    // =====================================================
    // LISTAR POR ESTADO
    // =====================================================

    @GetMapping("/estado/{estado}")
    public ResponseEntity<
            List<CronogramaActividadProyectoResponse>
            > listarPorEstado(

            @PathVariable String estado
    ) {

        return ResponseEntity.ok(

                actividadService
                        .listarPorEstado(
                                estado
                        )
        );
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<
            CronogramaActividadProyectoResponse
            > actualizarActividad(

            @PathVariable Long id,

            @RequestBody
            CronogramaActividadProyectoRequest request
    ) {

        return ResponseEntity.ok(

                actividadService
                        .actualizarActividad(
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
    eliminarActividad(

            @PathVariable Long id
    ) {

        actividadService
                .eliminarActividad(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}