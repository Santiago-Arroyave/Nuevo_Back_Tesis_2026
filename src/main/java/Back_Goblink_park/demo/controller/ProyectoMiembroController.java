package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.ProyectoMiembroRequest;

import Back_Goblink_park.demo.dto.response.ProyectoMiembroResponse;

import Back_Goblink_park.demo.service.interfaces.ProyectoMiembroService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyecto-miembros")
@RequiredArgsConstructor
public class ProyectoMiembroController {

    // =====================================================
    // SERVICE
    // =====================================================

    private final ProyectoMiembroService proyectoMiembroService;

    // =====================================================
    // AGREGAR MIEMBRO
    // =====================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProyectoMiembroResponse agregarMiembro(

            @RequestBody
            ProyectoMiembroRequest request
    ) {

        return proyectoMiembroService
                .agregarMiembro(request);
    }

    // =====================================================
    // LISTAR MIEMBROS DE PROYECTO
    // =====================================================

    @GetMapping("/proyecto/{proyectoId}")
    public List<ProyectoMiembroResponse>
    listarMiembrosProyecto(

            @PathVariable
            Long proyectoId
    ) {

        return proyectoMiembroService
                .listarMiembrosProyecto(
                        proyectoId
                );
    }

    // =====================================================
    // LISTAR PROYECTOS DEL USUARIO
    // =====================================================

    @GetMapping("/usuario/{usuarioId}")
    public List<ProyectoMiembroResponse>
    listarProyectosUsuario(

            @PathVariable
            Long usuarioId
    ) {

        return proyectoMiembroService
                .listarProyectosUsuario(
                        usuarioId
                );
    }

    // =====================================================
    // OBTENER MIEMBRO
    // =====================================================

    @GetMapping("/{id}")
    public ProyectoMiembroResponse obtenerMiembro(

            @PathVariable
            Long id
    ) {

        return proyectoMiembroService
                .obtenerMiembro(id);
    }

    // =====================================================
    // ELIMINAR MIEMBRO
    // =====================================================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarMiembro(

            @PathVariable
            Long id
    ) {

        proyectoMiembroService
                .eliminarMiembro(id);
    }
}