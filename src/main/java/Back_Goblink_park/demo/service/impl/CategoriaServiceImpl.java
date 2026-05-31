package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.CategoriaMapper;
import Back_Goblink_park.demo.dto.request.CategoriaRequest;
import Back_Goblink_park.demo.dto.response.CategoriaResponse;
import Back_Goblink_park.demo.entity.Categoria;
import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.CategoriaRepository;
import Back_Goblink_park.demo.service.interfaces.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // =====================================================
    // CREAR CATEGORÍA
    // =====================================================
    @Override
    @Transactional
    public CategoriaResponse crear(CategoriaRequest request, MultipartFile imagen) {
        // Verificar si ya existe una categoría con ese nombre
        if (categoriaRepository.existsByNombre(request.getNombre())) {
            throw new BusinessException("La categoría con ese nombre ya existe");
        }

        String imagenUrl = null;
        String imagenBase64 = null;

        // Procesar imagen si viene en el request
        if (imagen != null && !imagen.isEmpty()) {
            // Generar URL (puede ser fake o real si configuras storage)
            imagenUrl = generarUrlImagen(imagen);

            // Convertir a Base64 para mostrar en frontend
            try {
                byte[] bytes = imagen.getBytes();
                imagenBase64 = Base64.getEncoder().encodeToString(bytes);
                // Limpiar: quitar saltos de línea o espacios que pueda agregar Base64
                imagenBase64 = imagenBase64.replaceAll("\\s", "");
            } catch (IOException e) {
                throw new RuntimeException("Error al convertir la imagen a Base64: " + e.getMessage(), e);
            }
        }

        Categoria categoria = Categoria.builder()
                .nombre(request.getNombre().trim())
                .descripcion(request.getDescripcion() != null ? request.getDescripcion().trim() : null)
                .color(request.getColor())
                .estado(request.getEstado() != null ? request.getEstado() : true)
                .imagenUrl(imagenUrl)
                .imagenBase64(imagenBase64)
                .build();

        Categoria guardada = categoriaRepository.save(categoria);
        return CategoriaMapper.toResponse(guardada);
    }

    // =====================================================
    // ACTUALIZAR CATEGORÍA
    // =====================================================
    @Override
    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest request, MultipartFile imagen) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        // Actualizar campos de texto solo si no son null
        if (request.getNombre() != null) {
            categoria.setNombre(request.getNombre().trim());
        }
        if (request.getDescripcion() != null) {
            categoria.setDescripcion(request.getDescripcion().trim());
        }
        if (request.getColor() != null) {
            categoria.setColor(request.getColor());
        }
        if (request.getEstado() != null) {
            categoria.setEstado(request.getEstado());
        }

        // Actualizar imagen solo si se envía una nueva
        if (imagen != null && !imagen.isEmpty()) {
            // Generar nueva URL
            categoria.setImagenUrl(generarUrlImagen(imagen));

            // Convertir nueva imagen a Base64
            try {
                byte[] bytes = imagen.getBytes();
                String nuevaBase64 = Base64.getEncoder().encodeToString(bytes);
                categoria.setImagenBase64(nuevaBase64.replaceAll("\\s", ""));
            } catch (IOException e) {
                throw new RuntimeException("Error al convertir la imagen a Base64: " + e.getMessage(), e);
            }
        }

        Categoria actualizada = categoriaRepository.save(categoria);
        return CategoriaMapper.toResponse(actualizada);
    }

    // =====================================================
    // LISTAR CATEGORÍAS
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaMapper::toResponse)
                .toList();
    }

    // =====================================================
    // OBTENER CATEGORÍA POR ID
    // =====================================================
    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse obtenerPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        return CategoriaMapper.toResponse(categoria);
    }

    // =====================================================
    // ELIMINAR CATEGORÍA (LÓGICO)
    // =====================================================
    @Override
    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        // Eliminación lógica: marcar como inactiva en lugar de borrar
        categoria.setEstado(false);
        categoriaRepository.save(categoria);
    }

    // =====================================================
    // MÉTODO AUXILIAR: GENERAR URL DE IMAGEN
    // =====================================================
    /**
     * Genera una URL para la imagen.
     * TODO: Reemplazar con subida real a Supabase Storage, S3, Cloudinary, etc.
     */
    private String generarUrlImagen(MultipartFile file) {
        // Opción temporal: URL fake (NO funcional para mostrar)
        // return "https://storage.com/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Opción recomendada: Retornar null y usar solo Base64 en frontend
        return null;
    }

    // =====================================================
    // MÉTODO AUXILIAR: VALIDAR BASE64 (OPCIONAL)
    // =====================================================
    /**
     * Valida si un string es Base64 válido.
     * Útil para debugging.
     */
    @SuppressWarnings("unused")
    private boolean isValidBase64(String base64) {
        if (base64 == null || base64.isEmpty()) {
            return false;
        }
        try {
            Base64.getDecoder().decode(base64);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}