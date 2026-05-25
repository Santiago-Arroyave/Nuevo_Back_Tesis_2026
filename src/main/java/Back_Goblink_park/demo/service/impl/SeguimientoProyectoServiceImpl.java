package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.SeguimientoProyectoMapper;

import Back_Goblink_park.demo.dto.request.SeguimientoProyectoRequest;

import Back_Goblink_park.demo.dto.response.SeguimientoProyectoResponse;

import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.SeguimientoProyecto;
import Back_Goblink_park.demo.entity.Usuario;

import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.ProyectoRepository;
import Back_Goblink_park.demo.repository.SeguimientoProyectoRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;

import Back_Goblink_park.demo.service.interfaces.SeguimientoProyectoService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeguimientoProyectoServiceImpl
        implements SeguimientoProyectoService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final SeguimientoProyectoRepository
            seguimientoRepository;

    private final ProyectoRepository
            proyectoRepository;

    private final UsuarioRepository
            usuarioRepository;

    // =====================================================
    // CREAR
    // =====================================================

    @Override
    public SeguimientoProyectoResponse crearSeguimiento(
            SeguimientoProyectoRequest request
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
        // BUSCAR USUARIO
        // =============================================

        Usuario usuario = usuarioRepository
                .findById(request.getUsuarioId())

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        // =============================================
        // CREAR ENTITY
        // =============================================

        SeguimientoProyecto seguimiento =
                SeguimientoProyecto.builder()

                        .proyecto(proyecto)

                        .usuario(usuario)

                        .descripcionAvance(
                                request.getDescripcionAvance()
                        )

                        .porcentajeAvance(
                                request.getPorcentajeAvance()
                        )

                        .observaciones(
                                request.getObservaciones()
                        )

                        .build();

        // =============================================
        // GUARDAR
        // =============================================

        SeguimientoProyecto guardado =
                seguimientoRepository.save(
                        seguimiento
                );

        // =============================================
        // RESPONSE
        // =============================================

        return SeguimientoProyectoMapper
                .toResponse(guardado);
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    @Override
    public List<SeguimientoProyectoResponse>
    listarSeguimientos() {

        return seguimientoRepository.findAll()

                .stream()

                .map(
                        SeguimientoProyectoMapper
                                ::toResponse
                )

                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    public SeguimientoProyectoResponse
    obtenerSeguimiento(Long id) {

        SeguimientoProyecto seguimiento =
                seguimientoRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Seguimiento no encontrado"
                                )
                        );

        return SeguimientoProyectoMapper
                .toResponse(seguimiento);
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @Override
    public SeguimientoProyectoResponse
    actualizarSeguimiento(
            Long id,
            SeguimientoProyectoRequest request
    ) {

        SeguimientoProyecto seguimiento =
                seguimientoRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Seguimiento no encontrado"
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
                .findById(request.getUsuarioId())

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        // =============================================
        // ACTUALIZAR
        // =============================================

        seguimiento.setProyecto(proyecto);

        seguimiento.setUsuario(usuario);

        seguimiento.setDescripcionAvance(
                request.getDescripcionAvance()
        );

        seguimiento.setPorcentajeAvance(
                request.getPorcentajeAvance()
        );

        seguimiento.setObservaciones(
                request.getObservaciones()
        );

        // =============================================
        // GUARDAR
        // =============================================

        SeguimientoProyecto actualizado =
                seguimientoRepository.save(
                        seguimiento
                );

        return SeguimientoProyectoMapper
                .toResponse(actualizado);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @Override
    public void eliminarSeguimiento(Long id) {

        SeguimientoProyecto seguimiento =
                seguimientoRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Seguimiento no encontrado"
                                )
                        );

        seguimientoRepository.delete(seguimiento);
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @Override
    public List<SeguimientoProyectoResponse>
    listarPorProyecto(Long proyectoId) {

        return seguimientoRepository

                .findByProyectoIdOrderByFechaSeguimientoDesc(
                        proyectoId
                )

                .stream()

                .map(
                        SeguimientoProyectoMapper
                                ::toResponse
                )

                .toList();
    }

    // =====================================================
    // LISTAR POR USUARIO
    // =====================================================

    @Override
    public List<SeguimientoProyectoResponse>
    listarPorUsuario(Long usuarioId) {

        return seguimientoRepository

                .findByUsuarioIdOrderByFechaSeguimientoDesc(
                        usuarioId
                )

                .stream()

                .map(
                        SeguimientoProyectoMapper
                                ::toResponse
                )

                .toList();
    }

    // =====================================================
    // LISTAR POR PROYECTO Y USUARIO
    // =====================================================

    @Override
    public List<SeguimientoProyectoResponse>
    listarPorProyectoYUsuario(
            Long proyectoId,
            Long usuarioId
    ) {

        return seguimientoRepository

                .findByProyectoIdAndUsuarioId(
                        proyectoId,
                        usuarioId
                )

                .stream()

                .map(
                        SeguimientoProyectoMapper
                                ::toResponse
                )

                .toList();
    }

    // =====================================================
    // ÚLTIMOS SEGUIMIENTOS
    // =====================================================

    @Override
    public List<SeguimientoProyectoResponse>
    ultimosSeguimientos() {

        return seguimientoRepository

                .findTop10ByOrderByFechaSeguimientoDesc()

                .stream()

                .map(
                        SeguimientoProyectoMapper
                                ::toResponse
                )

                .toList();
    }
}