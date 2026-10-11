package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.PrioridadMapper;
import Back_Goblink_park.demo.dto.request.PrioridadRequest;
import Back_Goblink_park.demo.dto.response.PrioridadResponse;
import Back_Goblink_park.demo.entity.Prioridad;
import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.PrioridadRepository;
import Back_Goblink_park.demo.service.interfaces.FileStorageService;
import Back_Goblink_park.demo.service.interfaces.PrioridadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrioridadServiceImpl implements PrioridadService {

    private final PrioridadRepository prioridadRepository;
    private final FileStorageService fileStorageService;

    // =====================================================
    // CREAR PRIORIDAD
    // =====================================================
    @Override
    @Transactional
    public PrioridadResponse crear(PrioridadRequest request, MultipartFile imagen) {
        if (prioridadRepository.existsByNombre(request.getNombre())) {
            throw new BusinessException("La prioridad con ese nombre ya existe");
        }

        String urlImagen = null;
        if (imagen != null && !imagen.isEmpty()) {
            urlImagen = fileStorageService.uploadFile(imagen, "prioridades");
        }

        Prioridad prioridad = Prioridad.builder()
                .nombre(request.getNombre())
                .nivel(request.getNivel())
                .descripcion(request.getDescripcion())
                .color(request.getColor())
                .estado(request.getEstado() != null ? request.getEstado() : true)
                .imagenUrl(urlImagen)
                .imagenBase64(null)
                .build();

        Prioridad guardada = prioridadRepository.save(prioridad);
        return PrioridadMapper.toResponse(guardada);
    }

    // =====================================================
    // ACTUALIZAR PRIORIDAD
    // =====================================================
    @Override
    @Transactional
    public PrioridadResponse actualizar(Long id, PrioridadRequest request, MultipartFile imagen) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prioridad no encontrada"));

        prioridad.setNombre(request.getNombre());
        prioridad.setNivel(request.getNivel());
        prioridad.setDescripcion(request.getDescripcion());

        if (request.getColor() != null && !request.getColor().isEmpty()) {
            prioridad.setColor(request.getColor());
        }

        if (request.getEstado() != null) {
            prioridad.setEstado(request.getEstado());
        }

        if (imagen != null && !imagen.isEmpty()) {
            String url = fileStorageService.uploadFile(imagen, "prioridades");
            prioridad.setImagenUrl(url);
            prioridad.setImagenBase64(null);
        }

        Prioridad actualizada = prioridadRepository.save(prioridad);
        return PrioridadMapper.toResponse(actualizada);
    }

    // =====================================================
    // LISTAR
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<PrioridadResponse> listar() {
        return prioridadRepository.findAll()
                .stream()
                .map(PrioridadMapper::toResponse)
                .toList();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public PrioridadResponse obtenerPorId(Long id) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prioridad no encontrada"));
        return PrioridadMapper.toResponse(prioridad);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================
    @Override
    @Transactional
    public void eliminar(Long id) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prioridad no encontrada"));

        if (prioridad.getImagenUrl() != null && !prioridad.getImagenUrl().isBlank()) {
            fileStorageService.deleteFile(prioridad.getImagenUrl());
        }

        prioridadRepository.delete(prioridad);
    }
}