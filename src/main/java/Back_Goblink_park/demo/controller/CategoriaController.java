package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.CategoriaRequest;
import Back_Goblink_park.demo.dto.response.CategoriaResponse;
import Back_Goblink_park.demo.service.interfaces.CategoriaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // =====================================================
    // CREAR CATEGORÍA - SOLO ADMIN
    // =====================================================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoriaResponse> crear(
            @RequestParam @NotBlank(message = "El nombre es obligatorio")
            @Size(max = 100, message = "El nombre no puede exceder 100 caracteres") String nombre,

            @RequestParam(required = false) @Size(max = 500) String descripcion,

            @RequestParam(required = false, defaultValue = "true") String estado,

            @RequestParam(required = false) MultipartFile imagen,

            @RequestParam(required = false) @Size(max = 7) String color
    ) {
        // Convertir string a boolean para el estado
        boolean estadoBool = "activo".equalsIgnoreCase(estado) || "true".equalsIgnoreCase(estado);

        CategoriaRequest request = CategoriaRequest.builder()
                .nombre(nombre.trim())
                .descripcion(descripcion != null ? descripcion.trim() : null)
                .estado(estadoBool)
                .color(color)
                .build();

        CategoriaResponse response = categoriaService.crear(request, imagen);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =====================================================
    // ACTUALIZAR CATEGORÍA - SOLO ADMIN
    // =====================================================
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoriaResponse> actualizar(
            @PathVariable Long id,

            @RequestParam(required = false) @Size(max = 100) String nombre,

            @RequestParam(required = false) @Size(max = 500) String descripcion,

            @RequestParam(required = false) String estado,

            @RequestParam(required = false) MultipartFile imagen,

            @RequestParam(required = false) @Size(max = 7) String color
    ) {
        // Construir request solo con los campos que se enviaron
        CategoriaRequest.CategoriaRequestBuilder builder = CategoriaRequest.builder();

        if (nombre != null) builder.nombre(nombre.trim());
        if (descripcion != null) builder.descripcion(descripcion.trim());
        if (estado != null) {
            boolean estadoBool = "activo".equalsIgnoreCase(estado) || "true".equalsIgnoreCase(estado);
            builder.estado(estadoBool);
        }
        if (color != null) builder.color(color);

        CategoriaRequest request = builder.build();

        CategoriaResponse response = categoriaService.actualizar(id, request, imagen);
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // LISTAR CATEGORÍAS - ADMIN/USER
    // =====================================================
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    // =====================================================
    // OBTENER CATEGORÍA POR ID - ADMIN/USER
    // =====================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<CategoriaResponse> obtenerCategoriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    // =====================================================
    // ELIMINAR CATEGORÍA (LÓGICO) - SOLO ADMIN
    // =====================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}