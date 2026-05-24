package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.PrioridadMapper;
import Back_Goblink_park.demo.dto.request.PrioridadRequest;
import Back_Goblink_park.demo.dto.response.PrioridadResponse;
import Back_Goblink_park.demo.entity.Prioridad;
import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.PrioridadRepository;
import Back_Goblink_park.demo.service.interfaces.PrioridadService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrioridadServiceImpl implements PrioridadService {

    private final PrioridadRepository prioridadRepository;

    @Override
    public PrioridadResponse crear(PrioridadRequest request) {

        if (prioridadRepository.existsByNombre(request.getNombre())) {

            throw new BusinessException(
                    "La prioridad ya existe"
            );
        }

        Prioridad prioridad = Prioridad.builder()
                .nombre(request.getNombre())
                .nivel(request.getNivel())
                .descripcion(request.getDescripcion())
                .estado(
                        request.getEstado() != null
                                ? request.getEstado()
                                : true
                )
                .build();

        Prioridad guardada =
                prioridadRepository.save(prioridad);

        return PrioridadMapper.toResponse(guardada);
    }

    @Override
    public List<PrioridadResponse> listar() {

        return prioridadRepository.findAll()
                .stream()
                .map(PrioridadMapper::toResponse)
                .toList();
    }

    @Override
    public PrioridadResponse obtener(Long id) {

        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Prioridad no encontrada"
                        )
                );

        return PrioridadMapper.toResponse(prioridad);
    }

    @Override
    public PrioridadResponse actualizar(
            Long id,
            PrioridadRequest request
    ) {

        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Prioridad no encontrada"
                        )
                );

        prioridad.setNombre(request.getNombre());
        prioridad.setNivel(request.getNivel());
        prioridad.setDescripcion(request.getDescripcion());
        prioridad.setEstado(request.getEstado());

        Prioridad actualizada =
                prioridadRepository.save(prioridad);

        return PrioridadMapper.toResponse(actualizada);
    }

    @Override
    public void eliminar(Long id) {

        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Prioridad no encontrada"
                        )
                );

        prioridadRepository.delete(prioridad);
    }
}