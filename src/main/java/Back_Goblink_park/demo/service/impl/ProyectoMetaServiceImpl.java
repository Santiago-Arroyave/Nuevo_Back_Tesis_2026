package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ProyectoMetaMapper;
import Back_Goblink_park.demo.dto.request.ProyectoMetaRequest;
import Back_Goblink_park.demo.dto.response.ProyectoMetaResponse;

import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.ProyectoMeta;

import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.ProyectoMetaRepository;
import Back_Goblink_park.demo.repository.ProyectoRepository;

import Back_Goblink_park.demo.service.interfaces.ProyectoMetaService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoMetaServiceImpl
        implements ProyectoMetaService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final ProyectoMetaRepository
            proyectoMetaRepository;

    private final ProyectoRepository
            proyectoRepository;

    // =====================================================
    // CREAR META
    // =====================================================

    @Override
    public ProyectoMetaResponse crearMeta(
            ProyectoMetaRequest request
    ) {

        // =============================================
        // BUSCAR PROYECTO
        // =============================================

        Proyecto proyecto =
                proyectoRepository.findById(
                                request.getProyectoId()
                        )

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Proyecto no encontrado"
                                )
                        );

        // =============================================
        // CREAR META
        // =============================================

        ProyectoMeta meta =
                ProyectoMeta.builder()

                        .proyecto(
                                proyecto
                        )

                        .descripcion(
                                request.getDescripcion()
                        )

                        .build();

        // =============================================
        // GUARDAR
        // =============================================

        ProyectoMeta guardada =
                proyectoMetaRepository.save(
                        meta
                );

        // =============================================
        // RESPONSE
        // =============================================

        return ProyectoMetaMapper
                .toResponse(guardada);
    }

    // =====================================================
    // LISTAR TODAS
    // =====================================================

    @Override
    public List<ProyectoMetaResponse> listarMetas() {

        return proyectoMetaRepository.findAll()

                .stream()

                .map(
                        ProyectoMetaMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    public ProyectoMetaResponse obtenerMeta(
            Long id
    ) {

        ProyectoMeta meta =
                proyectoMetaRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Meta no encontrada"
                                )
                        );

        return ProyectoMetaMapper
                .toResponse(meta);
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @Override
    public List<ProyectoMetaResponse> listarPorProyecto(
            Long proyectoId
    ) {

        return proyectoMetaRepository

                .findByProyectoId(
                        proyectoId
                )

                .stream()

                .map(
                        ProyectoMetaMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // ACTUALIZAR META
    // =====================================================

    @Override
    public ProyectoMetaResponse actualizarMeta(
            Long id,
            ProyectoMetaRequest request
    ) {

        // =============================================
        // BUSCAR META
        // =============================================

        ProyectoMeta meta =
                proyectoMetaRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Meta no encontrada"
                                )
                        );

        // =============================================
        // BUSCAR PROYECTO
        // =============================================

        Proyecto proyecto =
                proyectoRepository.findById(
                                request.getProyectoId()
                        )

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Proyecto no encontrado"
                                )
                        );

        // =============================================
        // ACTUALIZAR
        // =============================================

        meta.setProyecto(
                proyecto
        );

        meta.setDescripcion(
                request.getDescripcion()
        );

        // =============================================
        // GUARDAR
        // =============================================

        ProyectoMeta actualizada =
                proyectoMetaRepository.save(
                        meta
                );

        return ProyectoMetaMapper
                .toResponse(actualizada);
    }

    // =====================================================
    // ELIMINAR META
    // =====================================================

    @Override
    public void eliminarMeta(
            Long id
    ) {

        ProyectoMeta meta =
                proyectoMetaRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Meta no encontrada"
                                )
                        );

        proyectoMetaRepository.delete(
                meta
        );
    }
}