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

import java.util.List;

@Service
@RequiredArgsConstructor

public class CategoriaServiceImpl
        implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // =====================================================
    // CREAR
    // =====================================================

    @Override
    public CategoriaResponse crear(
            CategoriaRequest request
    ) {

        if (categoriaRepository.existsByNombre(
                request.getNombre()
        )) {

            throw new BusinessException(
                    "La categoría ya existe"
            );
        }

        Categoria categoria = Categoria.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .estado(
                        request.getEstado() != null
                                ? request.getEstado()
                                : true
                )
                .build();

        Categoria guardada =
                categoriaRepository.save(categoria);

        return CategoriaMapper.toResponse(
                guardada
        );
    }

    // =====================================================
    // LISTAR
    // =====================================================

    @Override
    public List<CategoriaResponse> listar() {

        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaMapper::toResponse)
                .toList();
    }

    // =====================================================
    // OBTENER
    // =====================================================

    @Override
    public CategoriaResponse obtenerPorId(
            Long id
    ) {

        Categoria categoria =
                categoriaRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Categoría no encontrada"
                                )
                        );

        return CategoriaMapper.toResponse(
                categoria
        );
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @Override
    public CategoriaResponse actualizar(
            Long id,
            CategoriaRequest request
    ) {

        Categoria categoria =
                categoriaRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Categoría no encontrada"
                                )
                        );

        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(
                request.getDescripcion()
        );
        categoria.setEstado(request.getEstado());

        Categoria actualizada =
                categoriaRepository.save(categoria);

        return CategoriaMapper.toResponse(
                actualizada
        );
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @Override
    public void eliminar(Long id) {

        Categoria categoria =
                categoriaRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Categoría no encontrada"
                                )
                        );

        categoriaRepository.delete(categoria);
    }
}