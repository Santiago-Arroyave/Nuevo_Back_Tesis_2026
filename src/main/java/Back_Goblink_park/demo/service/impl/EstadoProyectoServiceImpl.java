package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.EstadoProyectoMapper;
import Back_Goblink_park.demo.dto.request.EstadoProyectoRequest;
import Back_Goblink_park.demo.dto.response.EstadoProyectoResponse;

import Back_Goblink_park.demo.entity.EstadoProyecto;

import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;

import Back_Goblink_park.demo.repository.EstadoProyectoRepository;

import Back_Goblink_park.demo.service.interfaces.EstadoProyectoService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoProyectoServiceImpl
        implements EstadoProyectoService {

    private final EstadoProyectoRepository repository;

    // =====================================================
    // CREAR
    // =====================================================

    @Override
    public EstadoProyectoResponse crear(
            EstadoProyectoRequest request
    ) {

        if (repository.existsByNombre(
                request.getNombre()
        )) {

            throw new BusinessException(
                    "El estado proyecto ya existe"
            );
        }

        EstadoProyecto estadoProyecto =
                EstadoProyecto.builder()
                        .nombre(request.getNombre())
                        .descripcion(request.getDescripcion())
                        .color(request.getColor())
                        .ordenVisual(request.getOrdenVisual())
                        .estado(
                                request.getEstado() != null
                                        ? request.getEstado()
                                        : true
                        )
                        .build();

        return EstadoProyectoMapper.toResponse(
                repository.save(estadoProyecto)
        );
    }

    // =====================================================
    // LISTAR
    // =====================================================

    @Override
    public List<EstadoProyectoResponse> listar() {

        return repository.findAll()
                .stream()
                .map(EstadoProyectoMapper::toResponse)
                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================

    @Override
    public EstadoProyectoResponse obtenerPorId(Long id) {

        EstadoProyecto estadoProyecto =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Estado proyecto no encontrado"
                                )
                        );

        return EstadoProyectoMapper.toResponse(
                estadoProyecto
        );
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @Override
    public EstadoProyectoResponse actualizar(
            Long id,
            EstadoProyectoRequest request
    ) {

        EstadoProyecto estadoProyecto =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Estado proyecto no encontrado"
                                )
                        );

        estadoProyecto.setNombre(request.getNombre());
        estadoProyecto.setDescripcion(request.getDescripcion());
        estadoProyecto.setColor(request.getColor());
        estadoProyecto.setOrdenVisual(request.getOrdenVisual());
        estadoProyecto.setEstado(request.getEstado());

        return EstadoProyectoMapper.toResponse(
                repository.save(estadoProyecto)
        );
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @Override
    public void eliminar(Long id) {

        EstadoProyecto estadoProyecto =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Estado proyecto no encontrado"
                                )
                        );

        repository.delete(estadoProyecto);
    }
}