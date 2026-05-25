package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ResponsableProyectoMapper;

import Back_Goblink_park.demo.dto.request.ResponsableProyectoRequest;

import Back_Goblink_park.demo.dto.response.ResponsableProyectoResponse;

import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.ResponsableProyecto;
import Back_Goblink_park.demo.entity.Usuario;

import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.ProyectoRepository;
import Back_Goblink_park.demo.repository.ResponsableProyectoRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;

import Back_Goblink_park.demo.service.interfaces.ResponsableProyectoService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponsableProyectoServiceImpl
        implements ResponsableProyectoService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final ResponsableProyectoRepository
            responsableRepository;

    private final ProyectoRepository
            proyectoRepository;

    private final UsuarioRepository
            usuarioRepository;

    // =====================================================
    // CREAR
    // =====================================================

    @Override
    public ResponsableProyectoResponse crearResponsable(
            ResponsableProyectoRequest request
    ) {

        // =============================================
        // BUSCAR PROYECTO
        // =============================================

        Proyecto proyecto = proyectoRepository
                .findById(request.getProyectoId())

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Proyecto no encontrado"
                        )
                );

        // =============================================
        // BUSCAR USUARIO RESPONSABLE
        // =============================================

        Usuario usuario = usuarioRepository
                .findById(
                        request.getUsuarioResponsableId()
                )

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        // =============================================
        // CREAR ENTITY
        // =============================================

        ResponsableProyecto responsable =
                ResponsableProyecto.builder()

                        .proyecto(proyecto)

                        .usuarioResponsable(usuario)

                        .titulo(
                                request.getTitulo()
                        )

                        .descripcion(
                                request.getDescripcion()
                        )

                        .estado(
                                request.getEstado()
                        )

                        .prioridad(
                                request.getPrioridad()
                        )

                        .fechaInicio(
                                request.getFechaInicio()
                        )

                        .fechaLimite(
                                request.getFechaLimite()
                        )

                        .porcentajeAvance(
                                request.getPorcentajeAvance()
                        )

                        .build();

        // =============================================
        // GUARDAR
        // =============================================

        ResponsableProyecto guardado =
                responsableRepository.save(
                        responsable
                );

        // =============================================
        // RESPONSE
        // =============================================

        return ResponsableProyectoMapper
                .toResponse(guardado);
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    @Override
    public List<ResponsableProyectoResponse>
    listarResponsables() {

        return responsableRepository.findAll()

                .stream()

                .map(
                        ResponsableProyectoMapper
                                ::toResponse
                )

                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    public ResponsableProyectoResponse
    obtenerResponsable(Long id) {

        ResponsableProyecto responsable =
                responsableRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Responsable no encontrado"
                                )
                        );

        return ResponsableProyectoMapper
                .toResponse(responsable);
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @Override
    public ResponsableProyectoResponse
    actualizarResponsable(
            Long id,
            ResponsableProyectoRequest request
    ) {

        ResponsableProyecto responsable =
                responsableRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Responsable no encontrado"
                                )
                        );

        // =============================================
        // BUSCAR PROYECTO
        // =============================================

        Proyecto proyecto = proyectoRepository
                .findById(request.getProyectoId())

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Proyecto no encontrado"
                        )
                );

        // =============================================
        // BUSCAR USUARIO
        // =============================================

        Usuario usuario = usuarioRepository
                .findById(
                        request.getUsuarioResponsableId()
                )

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        // =============================================
        // ACTUALIZAR
        // =============================================

        responsable.setProyecto(proyecto);

        responsable.setUsuarioResponsable(usuario);

        responsable.setTitulo(
                request.getTitulo()
        );

        responsable.setDescripcion(
                request.getDescripcion()
        );

        responsable.setEstado(
                request.getEstado()
        );

        responsable.setPrioridad(
                request.getPrioridad()
        );

        responsable.setFechaInicio(
                request.getFechaInicio()
        );

        responsable.setFechaLimite(
                request.getFechaLimite()
        );

        responsable.setPorcentajeAvance(
                request.getPorcentajeAvance()
        );

        // =============================================
        // GUARDAR
        // =============================================

        ResponsableProyecto actualizado =
                responsableRepository.save(
                        responsable
                );

        return ResponsableProyectoMapper
                .toResponse(actualizado);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @Override
    public void eliminarResponsable(Long id) {

        ResponsableProyecto responsable =
                responsableRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Responsable no encontrado"
                                )
                        );

        responsableRepository.delete(responsable);
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @Override
    public List<ResponsableProyectoResponse>
    listarPorProyecto(Long proyectoId) {

        return responsableRepository

                .findByProyectoId(proyectoId)

                .stream()

                .map(
                        ResponsableProyectoMapper
                                ::toResponse
                )

                .toList();
    }

    // =====================================================
    // LISTAR POR RESPONSABLE
    // =====================================================

    @Override
    public List<ResponsableProyectoResponse>
    listarPorUsuarioResponsable(
            Long usuarioId
    ) {

        return responsableRepository

                .findByUsuarioResponsableId(
                        usuarioId
                )

                .stream()

                .map(
                        ResponsableProyectoMapper
                                ::toResponse
                )

                .toList();
    }

    // =====================================================
    // LISTAR POR ESTADO
    // =====================================================

    @Override
    public List<ResponsableProyectoResponse>
    listarPorEstado(String estado) {

        return responsableRepository

                .findByEstado(estado)

                .stream()

                .map(
                        ResponsableProyectoMapper
                                ::toResponse
                )

                .toList();
    }

    // =====================================================
    // LISTAR POR PRIORIDAD
    // =====================================================

    @Override
    public List<ResponsableProyectoResponse>
    listarPorPrioridad(String prioridad) {

        return responsableRepository

                .findByPrioridad(prioridad)

                .stream()

                .map(
                        ResponsableProyectoMapper
                                ::toResponse
                )

                .toList();
    }
}