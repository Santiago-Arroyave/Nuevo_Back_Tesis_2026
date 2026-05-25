package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.ProyectoPresupuestoMapper;
import Back_Goblink_park.demo.dto.request.ProyectoPresupuestoRequest;
import Back_Goblink_park.demo.dto.response.ProyectoPresupuestoResponse;

import Back_Goblink_park.demo.entity.Proyecto;
import Back_Goblink_park.demo.entity.ProyectoPresupuesto;

import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.ProyectoPresupuestoRepository;
import Back_Goblink_park.demo.repository.ProyectoRepository;

import Back_Goblink_park.demo.service.interfaces.ProyectoPresupuestoService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoPresupuestoServiceImpl
        implements ProyectoPresupuestoService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final ProyectoPresupuestoRepository
            proyectoPresupuestoRepository;

    private final ProyectoRepository
            proyectoRepository;

    // =====================================================
    // CREAR PRESUPUESTO
    // =====================================================

    @Override
    public ProyectoPresupuestoResponse crearPresupuesto(
            ProyectoPresupuestoRequest request
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
        // CREAR PRESUPUESTO
        // =============================================

        ProyectoPresupuesto presupuesto =
                ProyectoPresupuesto.builder()

                        .proyecto(
                                proyecto
                        )

                        .rubro(
                                request.getRubro()
                        )

                        .monto(
                                request.getMonto()
                        )

                        .observaciones(
                                request.getObservaciones()
                        )

                        .build();

        // =============================================
        // GUARDAR
        // =============================================

        ProyectoPresupuesto guardado =
                proyectoPresupuestoRepository.save(
                        presupuesto
                );

        // =============================================
        // RESPONSE
        // =============================================

        return ProyectoPresupuestoMapper
                .toResponse(guardado);
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    @Override
    public List<ProyectoPresupuestoResponse>
    listarPresupuestos() {

        return proyectoPresupuestoRepository.findAll()

                .stream()

                .map(
                        ProyectoPresupuestoMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    public ProyectoPresupuestoResponse obtenerPresupuesto(
            Long id
    ) {

        ProyectoPresupuesto presupuesto =
                proyectoPresupuestoRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Presupuesto no encontrado"
                                )
                        );

        return ProyectoPresupuestoMapper
                .toResponse(presupuesto);
    }

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    @Override
    public List<ProyectoPresupuestoResponse>
    listarPorProyecto(
            Long proyectoId
    ) {

        return proyectoPresupuestoRepository

                .findByProyectoId(
                        proyectoId
                )

                .stream()

                .map(
                        ProyectoPresupuestoMapper::toResponse
                )

                .toList();
    }

    // =====================================================
    // ACTUALIZAR PRESUPUESTO
    // =====================================================

    @Override
    public ProyectoPresupuestoResponse actualizarPresupuesto(
            Long id,
            ProyectoPresupuestoRequest request
    ) {

        // =============================================
        // BUSCAR PRESUPUESTO
        // =============================================

        ProyectoPresupuesto presupuesto =
                proyectoPresupuestoRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Presupuesto no encontrado"
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

        presupuesto.setProyecto(
                proyecto
        );

        presupuesto.setRubro(
                request.getRubro()
        );

        presupuesto.setMonto(
                request.getMonto()
        );

        presupuesto.setObservaciones(
                request.getObservaciones()
        );

        // =============================================
        // GUARDAR
        // =============================================

        ProyectoPresupuesto actualizado =
                proyectoPresupuestoRepository.save(
                        presupuesto
                );

        return ProyectoPresupuestoMapper
                .toResponse(actualizado);
    }

    // =====================================================
    // ELIMINAR PRESUPUESTO
    // =====================================================

    @Override
    public void eliminarPresupuesto(
            Long id
    ) {

        ProyectoPresupuesto presupuesto =
                proyectoPresupuestoRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Presupuesto no encontrado"
                                )
                        );

        proyectoPresupuestoRepository.delete(
                presupuesto
        );
    }
}