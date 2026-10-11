package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.mapper.CategoriaMapper;
import Back_Goblink_park.demo.dto.request.CategoriaRequest;
import Back_Goblink_park.demo.dto.response.CategoriaResponse;
import Back_Goblink_park.demo.entity.Categoria;
import Back_Goblink_park.demo.exception.BusinessException;
import Back_Goblink_park.demo.exception.ResourceNotFoundException;
import Back_Goblink_park.demo.repository.CategoriaRepository;
import Back_Goblink_park.demo.service.interfaces.CategoriaService;
import Back_Goblink_park.demo.service.interfaces.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final FileStorageService fileStorageService;

    // =====================================================
    // CREAR CATEGORÍA
    // =====================================================
    @Override
    @Transactional
    public CategoriaResponse crear(CategoriaRequest request, MultipartFile imagen) {
        if (categoriaRepository.existsByNombre(request.getNombre())) {
            throw new BusinessException("La categoría con ese nombre ya existe");
        }

        String imagenUrl = null;
        if (imagen != null && !imagen.isEmpty()) {
            imagenUrl = fileStorageService.uploadFile(imagen, "categorias");
        }

        Categoria categoria = Categoria.builder()
                .nombre(request.getNombre().trim())
                .descripcion(request.getDescripcion() != null ? request.getDescripcion().trim() : null)
                .color(request.getColor())
                .estado(request.getEstado() != null ? request.getEstado() : true)
                .imagenUrl(imagenUrl)
                .imagenBase64(null)
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

        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            if (!request.getNombre().trim().equalsIgnoreCase(categoria.getNombre())
                    && categoriaRepository.existsByNombre(request.getNombre().trim())) {
                throw new BusinessException("Ya existe otra categoría con el nombre: " + request.getNombre());
            }
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

        if (imagen != null && !imagen.isEmpty()) {
            String imagenUrl = fileStorageService.uploadFile(imagen, "categorias");
            categoria.setImagenUrl(imagenUrl);
            categoria.setImagenBase64(null);
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

        categoria.setEstado(false);
        categoriaRepository.save(categoria);
    }
}