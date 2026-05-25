package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ProyectoMetaRequest;
import Back_Goblink_park.demo.dto.response.ProyectoMetaResponse;
import Back_Goblink_park.demo.service.interfaces.ProyectoMetaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyecto-metas")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProyectoMetaController {

    // =====================================================
    // SERVICE
    // =====================================================

    private final ProyectoMetaService
            proyectoMetaService;

    // =====================================================
    // CREAR META
    // =====================================================

    @PostMapping
    public ResponseEntity<ProyectoMetaResponse>
    crearMeta(

            @RequestBody
            ProyectoMetaRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(

                        proyectoMetaService
                                .crearMeta(request)
                );
    }

    // =====================================================
    // LISTAR TODAS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<ProyectoMetaResponse>>
    listarMetas() {

        return ResponseEntity.ok(

                proyectoMetaService
                        .listarMetas()
        );
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoMetaResponse>
    obtenerMeta(

            @PathVariable Long id
    ) {

        return ResponseEntity.ok(

                proyectoMetaService
                        .obtenerMeta(id)
        );
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<ProyectoMetaResponse>>
    listarPorProyecto(

            @PathVariable Long proyectoId
    ) {

        return ResponseEntity.ok(

                proyectoMetaService
                        .listarPorProyecto(proyectoId)
        );
    }

    // =====================================================
    // ACTUALIZAR META
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoMetaResponse>
    actualizarMeta(

            @PathVariable Long id,

            @RequestBody
            ProyectoMetaRequest request
    ) {

        return ResponseEntity.ok(

                proyectoMetaService
                        .actualizarMeta(
                                id,
                                request
                        )
        );
    }

    // =====================================================
    // ELIMINAR META
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    eliminarMeta(

            @PathVariable Long id
    ) {

        proyectoMetaService
                .eliminarMeta(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}