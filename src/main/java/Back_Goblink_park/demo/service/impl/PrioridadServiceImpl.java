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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrioridadServiceImpl implements PrioridadService {

    private final PrioridadRepository prioridadRepository;

    // =====================================================
    // CREAR PRIORIDAD
    // =====================================================
    @Override
    public PrioridadResponse crear(PrioridadRequest request, MultipartFile imagen) {
        // Verificar si ya existe una prioridad con ese nombre
        if (prioridadRepository.existsByNombre(request.getNombre())) {
            throw new BusinessException("La prioridad con ese nombre ya existe");
        }

        String urlImagen = null;
        String base64Imagen = null;

        // Procesar la imagen si viene en el request
        if (imagen != null && !imagen.isEmpty()) {
            urlImagen = subirImagen(imagen);

            try {
                byte[] bytes = imagen.getBytes();
                base64Imagen = Base64.getEncoder().encodeToString(bytes);
                // Limpiar posibles espacios o saltos de línea
                base64Imagen = base64Imagen.replaceAll("\\s", "");
            } catch (IOException e) {
                throw new RuntimeException("Error al convertir la imagen a Base64", e);
            }
        }

        Prioridad prioridad = Prioridad.builder()
                .nombre(request.getNombre())
                .nivel(request.getNivel())
                .descripcion(request.getDescripcion())
                .color(request.getColor())  // ✅ AGREGADO: Asignar color
                .estado(request.getEstado() != null ? request.getEstado() : true)
                .imagenUrl(urlImagen)
                .imagenBase64(base64Imagen)
                .build();

        Prioridad guardada = prioridadRepository.save(prioridad);
        return PrioridadMapper.toResponse(guardada);
    }

    // =====================================================
    // ACTUALIZAR PRIORIDAD
    // =====================================================
    @Override
    public PrioridadResponse actualizar(Long id, PrioridadRequest request, MultipartFile imagen) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prioridad no encontrada"));

        prioridad.setNombre(request.getNombre());
        prioridad.setNivel(request.getNivel());
        prioridad.setDescripcion(request.getDescripcion());

        // ✅ AGREGADO: Actualizar color
        if (request.getColor() != null && !request.getColor().isEmpty()) {
            prioridad.setColor(request.getColor());
        }

        // Solo actualizamos estado si viene explícitamente en el request
        if (request.getEstado() != null) {
            prioridad.setEstado(request.getEstado());
        }

        // Solo actualizar imagen si se envía una nueva
        if (imagen != null && !imagen.isEmpty()) {
            prioridad.setImagenUrl(subirImagen(imagen));
            try {
                byte[] bytes = imagen.getBytes();
                String base64 = Base64.getEncoder().encodeToString(bytes);
                prioridad.setImagenBase64(base64.replaceAll("\\s", ""));
            } catch (IOException e) {
                throw new RuntimeException("Error al convertir la imagen a Base64", e);
            }
        }

        Prioridad actualizada = prioridadRepository.save(prioridad);
        return PrioridadMapper.toResponse(actualizada);
    }

    // =====================================================
    // LISTAR
    // =====================================================
    @Override
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
    public PrioridadResponse obtenerPorId(Long id) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prioridad no encontrada"));
        return PrioridadMapper.toResponse(prioridad);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================
    @Override
    public void eliminar(Long id) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prioridad no encontrada"));
        prioridadRepository.delete(prioridad);
    }

    // =====================================================
    // MÉTODO AUXILIAR
    // =====================================================
    private String subirImagen(MultipartFile file) {
        // TODO: Implementar subida real a S3/Supabase Storage si lo deseas
        return "https://storage.com/prioridades/" + file.getOriginalFilename();
    }
}