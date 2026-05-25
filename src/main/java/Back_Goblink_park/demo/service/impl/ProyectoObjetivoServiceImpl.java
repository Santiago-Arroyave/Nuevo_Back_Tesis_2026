package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ProyectoObjetivoMapper;
import Back_Goblink_park.demo.dto.request.ProyectoObjetivoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoObjetivoResponse;

import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.ProyectoObjetivo;

import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.ProyectoObjetivoRepository;
import Back_Goblink_park.demo.repository.ProyectoRepository;

import Back_Goblink_park.demo.service.interfaces.ProyectoObjetivoService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoObjetivoServiceImpl
        implements ProyectoObjetivoService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final ProyectoObjetivoRepository
            proyectoObjetivoRepository;

    private final ProyectoRepository
            proyectoRepository;

    // =====================================================
    // CREAR OBJETIVO
    // =====================================================

    @Override
    public ProyectoObjetivoResponse crearObjetivo(
            ProyectoObjetivoRequest request
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
        // CREAR OBJETIVO
        // =============================================

        ProyectoObjetivo objetivo =
                ProyectoObjetivo.builder()

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

        ProyectoObjetivo guardado =
                proyectoObjetivoRepository.save(
                        objetivo
                );

        // =============================================
        // RESPONSE
        // =============================================

        return ProyectoObjetivoMapper
                .toResponse(guardado);
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    @Override
    public List<ProyectoObjetivoResponse> listarObjetivos() {

        return proyectoObjetivoRepository.findAll()

                .stream()

                .map(
                        ProyectoObjetivoMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    public ProyectoObjetivoResponse obtenerObjetivo(
            Long id
    ) {

        ProyectoObjetivo objetivo =
                proyectoObjetivoRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Objetivo no encontrado"
                                )
                        );

        return ProyectoObjetivoMapper
                .toResponse(objetivo);
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @Override
    public List<ProyectoObjetivoResponse> listarPorProyecto(
            Long proyectoId
    ) {

        return proyectoObjetivoRepository

                .findByProyectoId(
                        proyectoId
                )

                .stream()

                .map(
                        ProyectoObjetivoMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // ACTUALIZAR OBJETIVO
    // =====================================================

    @Override
    public ProyectoObjetivoResponse actualizarObjetivo(
            Long id,
            ProyectoObjetivoRequest request
    ) {

        // =============================================
        // BUSCAR OBJETIVO
        // =============================================

        ProyectoObjetivo objetivo =
                proyectoObjetivoRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Objetivo no encontrado"
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

        objetivo.setProyecto(
                proyecto
        );

        objetivo.setDescripcion(
                request.getDescripcion()
        );

        // =============================================
        // GUARDAR
        // =============================================

        ProyectoObjetivo actualizado =
                proyectoObjetivoRepository.save(
                        objetivo
                );

        return ProyectoObjetivoMapper
                .toResponse(actualizado);
    }

    // =====================================================
    // ELIMINAR OBJETIVO
    // =====================================================

    @Override
    public void eliminarObjetivo(
            Long id
    ) {

        ProyectoObjetivo objetivo =
                proyectoObjetivoRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Objetivo no encontrado"
                                )
                        );

        proyectoObjetivoRepository.delete(
                objetivo
        );
    }
}