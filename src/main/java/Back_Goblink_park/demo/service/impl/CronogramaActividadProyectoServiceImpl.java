package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.CronogramaActividadProyectoMapper;

import Back_Goblink_park.demo.dto.request.CronogramaActividadProyectoRequest;

import Back_Goblink_park.demo.dto.response.CronogramaActividadProyectoResponse;

import Back_Goblink_park.demo.entity.CronogramaActividadProyecto;
import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.ResponsableProyecto;

import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.CronogramaActividadProyectoRepository;
import Back_Goblink_park.demo.repository.ProyectoRepository;
import Back_Goblink_park.demo.repository.ResponsableProyectoRepository;

import Back_Goblink_park.demo.service.interfaces.CronogramaActividadProyectoService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CronogramaActividadProyectoServiceImpl
        implements CronogramaActividadProyectoService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final CronogramaActividadProyectoRepository
            actividadRepository;

    private final ProyectoRepository
            proyectoRepository;

    private final ResponsableProyectoRepository
            responsableRepository;

    // =====================================================
    // CREAR
    // =====================================================

    @Override
    public CronogramaActividadProyectoResponse
    crearActividad(
            CronogramaActividadProyectoRequest request
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
        // BUSCAR RESPONSABLE
        // =============================================

        ResponsableProyecto responsable = null;

        if (request.getResponsableId() != null) {

            responsable = responsableRepository
                    .findById(
                            request.getResponsableId()
                    )

                    .orElseThrow(() ->

                            new ResourceNotFoundException(
                                    "Responsable no encontrado"
                            )
                    );
        }

        // =============================================
        // CREAR ENTITY
        // =============================================

        CronogramaActividadProyecto actividad =
                CronogramaActividadProyecto.builder()

                        .proyecto(proyecto)

                        .responsable(responsable)

                        .nombre(
                                request.getNombre()
                        )

                        .descripcion(
                                request.getDescripcion()
                        )

                        .estado(
                                request.getEstado()
                        )

                        .fechaInicio(
                                request.getFechaInicio()
                        )

                        .fechaFin(
                                request.getFechaFin()
                        )

                        .urlEvidencia(
                                request.getUrlEvidencia()
                        )

                        .observaciones(
                                request.getObservaciones()
                        )

                        .build();

        // =============================================
        // GUARDAR
        // =============================================

        CronogramaActividadProyecto guardada =
                actividadRepository.save(
                        actividad
                );

        // =============================================
        // RESPONSE
        // =============================================

        return CronogramaActividadProyectoMapper
                .toResponse(guardada);
    }

    // =====================================================
    // LISTAR TODAS
    // =====================================================

    @Override
    public List<CronogramaActividadProyectoResponse>
    listarActividades() {

        return actividadRepository.findAll()

                .stream()

                .map(
                        CronogramaActividadProyectoMapper
                                ::toResponse
                )

                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    public CronogramaActividadProyectoResponse
    obtenerActividad(Long id) {

        CronogramaActividadProyecto actividad =
                actividadRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Actividad no encontrada"
                                )
                        );

        return CronogramaActividadProyectoMapper
                .toResponse(actividad);
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @Override
    public List<CronogramaActividadProyectoResponse>
    listarPorProyecto(Long proyectoId) {

        return actividadRepository

                .findByProyectoIdOrderByFechaInicioAsc(
                        proyectoId
                )

                .stream()

                .map(
                        CronogramaActividadProyectoMapper
                                ::toResponse
                )

                .toList();
    }

    // =====================================================
    // LISTAR POR RESPONSABLE
    // =====================================================

    @Override
    public List<CronogramaActividadProyectoResponse>
    listarPorResponsable(Long responsableId) {

        return actividadRepository

                .findByResponsableId(
                        responsableId
                )

                .stream()

                .map(
                        CronogramaActividadProyectoMapper
                                ::toResponse
                )

                .toList();
    }

    // =====================================================
    // LISTAR POR ESTADO
    // =====================================================

    @Override
    public List<CronogramaActividadProyectoResponse>
    listarPorEstado(String estado) {

        return actividadRepository

                .findByEstado(estado)

                .stream()

                .map(
                        CronogramaActividadProyectoMapper
                                ::toResponse
                )

                .toList();
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @Override
    public CronogramaActividadProyectoResponse
    actualizarActividad(
            Long id,
            CronogramaActividadProyectoRequest request
    ) {

        CronogramaActividadProyecto actividad =
                actividadRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Actividad no encontrada"
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
        // BUSCAR RESPONSABLE
        // =============================================

        ResponsableProyecto responsable = null;

        if (request.getResponsableId() != null) {

            responsable = responsableRepository
                    .findById(
                            request.getResponsableId()
                    )

                    .orElseThrow(() ->

                            new ResourceNotFoundException(
                                    "Responsable no encontrado"
                            )
                    );
        }

        // =============================================
        // ACTUALIZAR
        // =============================================

        actividad.setProyecto(proyecto);

        actividad.setResponsable(responsable);

        actividad.setNombre(
                request.getNombre()
        );

        actividad.setDescripcion(
                request.getDescripcion()
        );

        actividad.setEstado(
                request.getEstado()
        );

        actividad.setFechaInicio(
                request.getFechaInicio()
        );

        actividad.setFechaFin(
                request.getFechaFin()
        );

        actividad.setUrlEvidencia(
                request.getUrlEvidencia()
        );

        actividad.setObservaciones(
                request.getObservaciones()
        );

        // =============================================
        // GUARDAR
        // =============================================

        CronogramaActividadProyecto actualizada =
                actividadRepository.save(
                        actividad
                );

        return CronogramaActividadProyectoMapper
                .toResponse(actualizada);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @Override
    public void eliminarActividad(Long id) {

        CronogramaActividadProyecto actividad =
                actividadRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Actividad no encontrada"
                                )
                        );

        actividadRepository.delete(actividad);
    }
}