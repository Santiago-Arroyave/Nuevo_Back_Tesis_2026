package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.CategoriaRequest;
import Back_Goblink_park.demo.dto.response.CategoriaResponse;
import Back_Goblink_park.demo.service.interfaces.CategoriaService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor

public class CategoriaController {

    private final CategoriaService categoriaService;

    // =====================================================
    // CREAR
    // =====================================================

    @PostMapping

    @ResponseStatus(HttpStatus.CREATED)

    @PreAuthorize("hasRole('ADMIN')")

    public CategoriaResponse crear(

            @Valid

            @RequestBody
            CategoriaRequest request
    ) {

        return categoriaService.crear(request);
    }

    // =====================================================
    // LISTAR
    // =====================================================

    @GetMapping

    @PreAuthorize(
            "hasAnyRole('ADMIN','USER')"
    )

    public List<CategoriaResponse> listar() {

        return categoriaService.listar();
    }

    // =====================================================
    // OBTENER
    // =====================================================

    @GetMapping("/{id}")

    @PreAuthorize(
            "hasAnyRole('ADMIN','USER')"
    )

    public CategoriaResponse obtenerPorId(

            @PathVariable Long id
    ) {

        return categoriaService.obtenerPorId(id);
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    @PutMapping("/{id}")

    @PreAuthorize("hasRole('ADMIN')")

    public CategoriaResponse actualizar(

            @PathVariable Long id,

            @Valid

            @RequestBody
            CategoriaRequest request
    ) {

        return categoriaService.actualizar(
                id,
                request
        );
    }

    // =====================================================
    // ELIMINAR
    // =====================================================

    @DeleteMapping("/{id}")

    @PreAuthorize("hasRole('ADMIN')")

    public void eliminar(
            @PathVariable Long id
    ) {

        categoriaService.eliminar(id);
    }
}