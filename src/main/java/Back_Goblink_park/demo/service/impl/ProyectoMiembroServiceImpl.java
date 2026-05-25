package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ProyectoMiembroMapper;

import Back_Goblink_park.demo.dto.request.ProyectoMiembroRequest;

import Back_Goblink_park.demo.dto.response.ProyectoMiembroResponse;

import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.ProyectoMiembro;
import Back_Goblink_park.demo.entity.Usuario;

import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.ProyectoMiembroRepository;
import Back_Goblink_park.demo.repository.ProyectoRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;

import Back_Goblink_park.demo.service.interfaces.ProyectoMiembroService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoMiembroServiceImpl
        implements ProyectoMiembroService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final ProyectoMiembroRepository miembroRepository;

    private final ProyectoRepository proyectoRepository;

    private final UsuarioRepository usuarioRepository;

    // =====================================================
    // AGREGAR MIEMBRO
    // =====================================================

    @Override
    public ProyectoMiembroResponse agregarMiembro(
            ProyectoMiembroRequest request
    ) {

        // ==============================================
        // VALIDAR PROYECTO
        // ==============================================

        Proyecto proyecto = proyectoRepository
                .findById(request.getProyectoId())

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Proyecto no encontrado"
                        )
                );

        // ==============================================
        // VALIDAR USUARIO
        // ==============================================

        Usuario usuario = usuarioRepository
                .findById(request.getUsuarioId())

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        // ==============================================
        // VALIDAR DUPLICADO
        // ==============================================

        boolean existe = miembroRepository

                .existsByProyectoIdAndUsuarioId(
                        request.getProyectoId(),
                        request.getUsuarioId()
                );

        if (existe) {

            throw new RuntimeException(
                    "El usuario ya pertenece al proyecto"
            );
        }

        // ==============================================
        // CREAR MIEMBRO
        // ==============================================

        ProyectoMiembro miembro =
                ProyectoMiembro.builder()

                        .proyecto(proyecto)

                        .usuario(usuario)

                        .rolEnProyecto(
                                request.getRolEnProyecto()
                        )

                        .build();

        // ==============================================
        // GUARDAR
        // ==============================================

        ProyectoMiembro guardado =
                miembroRepository.save(miembro);

        // ==============================================
        // RESPONSE
        // ==============================================

        return ProyectoMiembroMapper
                .toResponse(guardado);
    }

    // =====================================================
    // LISTAR MIEMBROS PROYECTO
    // =====================================================

    @Override
    public List<ProyectoMiembroResponse>
    listarMiembrosProyecto(
            Long proyectoId
    ) {

        return miembroRepository

                .findByProyectoId(proyectoId)

                .stream()

                .map(
                        ProyectoMiembroMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // LISTAR PROYECTOS USUARIO
    // =====================================================

    @Override
    public List<ProyectoMiembroResponse>
    listarProyectosUsuario(
            Long usuarioId
    ) {

        return miembroRepository

                .findByUsuarioId(usuarioId)

                .stream()

                .map(
                        ProyectoMiembroMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // OBTENER MIEMBRO
    // =====================================================

    @Override
    public ProyectoMiembroResponse obtenerMiembro(
            Long id
    ) {

        ProyectoMiembro miembro = miembroRepository
                .findById(id)

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Miembro no encontrado"
                        )
                );

        return ProyectoMiembroMapper
                .toResponse(miembro);
    }

    // =====================================================
    // ELIMINAR MIEMBRO
    // =====================================================

    @Override
    public void eliminarMiembro(
            Long id
    ) {

        ProyectoMiembro miembro = miembroRepository
                .findById(id)

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Miembro no encontrado"
                        )
                );

        miembroRepository.delete(miembro);
    }
}