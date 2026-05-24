package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.EstadoReporteMapper;
import Back_Goblink_park.demo.dto.request.EstadoReporteRequest;
import Back_Goblink_park.demo.dto.response.EstadoReporteResponse;
import Back_Goblink_park.demo.entity.EstadoReporte;
import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.EstadoReporteRepository;
import Back_Goblink_park.demo.service.interfaces.EstadoReporteService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoReporteServiceImpl
        implements EstadoReporteService {

    private final EstadoReporteRepository estadoReporteRepository;

    @Override
    public EstadoReporteResponse crear(
            EstadoReporteRequest request
    ) {

        if (estadoReporteRepository.existsByNombre(
                request.getNombre()
        )) {

            throw new BusinessException(
                    "El estado ya existe"
            );
        }

        EstadoReporte estado = EstadoReporte.builder()
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

        EstadoReporte guardado =
                estadoReporteRepository.save(estado);

        return EstadoReporteMapper.toResponse(
                guardado
        );
    }

    @Override
    public List<EstadoReporteResponse> listar() {

        return estadoReporteRepository.findAll()
                .stream()
                .map(EstadoReporteMapper::toResponse)
                .toList();
    }

    @Override
    public EstadoReporteResponse obtener(Long id) {

        EstadoReporte estado =
                estadoReporteRepository.findById(id)
                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Estado no encontrado"
                                )
                        );

        return EstadoReporteMapper.toResponse(
                estado
        );
    }

    @Override
    public EstadoReporteResponse actualizar(
            Long id,
            EstadoReporteRequest request
    ) {

        EstadoReporte estado =
                estadoReporteRepository.findById(id)
                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Estado no encontrado"
                                )
                        );

        estado.setNombre(request.getNombre());
        estado.setDescripcion(request.getDescripcion());
        estado.setColor(request.getColor());
        estado.setOrdenVisual(request.getOrdenVisual());
        estado.setEstado(request.getEstado());

        EstadoReporte actualizado =
                estadoReporteRepository.save(estado);

        return EstadoReporteMapper.toResponse(
                actualizado
        );
    }

    @Override
    public void eliminar(Long id) {

        EstadoReporte estado =
                estadoReporteRepository.findById(id)
                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Estado no encontrado"
                                )
                        );

        estadoReporteRepository.delete(estado);
    }
}