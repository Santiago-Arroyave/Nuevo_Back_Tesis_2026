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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProyectoObjetivoServiceImpl implements ProyectoObjetivoService {

    private final ProyectoObjetivoRepository objetivoRepository;
    private final ProyectoRepository proyectoRepository;

    @Override
    public ProyectoObjetivoResponse crearObjetivo(ProyectoObjetivoRequest request) {
        Proyecto proyecto = proyectoRepository.findById(request.getProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        ProyectoObjetivo objetivo = ProyectoObjetivo.builder()
                .proyecto(proyecto)
                .descripcion(request.getDescripcion())
                .build();

        ProyectoObjetivo guardado = objetivoRepository.save(objetivo);
        return ProyectoObjetivoMapper.toResponse(guardado);
    }

    @Override
    public List<ProyectoObjetivoResponse> listarObjetivosProyecto(Long proyectoId) {
        return objetivoRepository.findByProyectoId(proyectoId)
                .stream()
                .map(ProyectoObjetivoMapper::toResponse)
                .toList();
    }

    @Override
    public ProyectoObjetivoResponse obtenerObjetivo(Long id) {
        ProyectoObjetivo objetivo = objetivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Objetivo no encontrado"));
        return ProyectoObjetivoMapper.toResponse(objetivo);
    }

    @Override
    public ProyectoObjetivoResponse actualizarObjetivo(Long id, ProyectoObjetivoRequest request) {
        ProyectoObjetivo objetivo = objetivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Objetivo no encontrado"));
        objetivo.setDescripcion(request.getDescripcion());
        return ProyectoObjetivoMapper.toResponse(objetivoRepository.save(objetivo));
    }

    @Override
    public void eliminarObjetivo(Long id) {
        ProyectoObjetivo objetivo = objetivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Objetivo no encontrado"));
        // Soft delete: se podría agregar un booleano "estado" si lo quieres
        objetivoRepository.delete(objetivo);
    }
}