package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.PrioridadRequest;
import Back_Goblink_park.demo.dto.response.PrioridadResponse;
import Back_Goblink_park.demo.service.interfaces.PrioridadService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/prioridades")
@RequiredArgsConstructor
public class PrioridadController {

    private final PrioridadService prioridadService;

    // =====================================================
    // CREAR PRIORIDAD - SOLO ADMIN
    // =====================================================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public PrioridadResponse crearPrioridad(
            @RequestParam("nombre") @NotBlank @Size(max = 20) String nombre,
            @RequestParam("nivel") @NotNull @Min(1) @Max(4) Integer nivel,
            @RequestParam(value = "descripcion", required = false) @Size(max = 100) String descripcion,
            @RequestParam(value = "estado", defaultValue = "true") Boolean estado,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) {
        // Construimos el Request manualmente con los parámetros recibidos
        PrioridadRequest request = PrioridadRequest.builder()
                .nombre(nombre)
                .nivel(nivel)
                .descripcion(descripcion)
                .estado(estado)
                .build();

        return prioridadService.crear(request, imagen);
    }

    // =====================================================
    // ACTUALIZAR PRIORIDAD - SOLO ADMIN
    // =====================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PrioridadResponse actualizarPrioridad(
            @PathVariable Long id,
            @RequestParam("nombre") @NotBlank @Size(max = 20) String nombre,
            @RequestParam("nivel") @NotNull @Min(1) @Max(4) Integer nivel,
            @RequestParam(value = "descripcion", required = false) @Size(max = 100) String descripcion,
            @RequestParam(value = "estado", defaultValue = "true") Boolean estado,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) {
        PrioridadRequest request = PrioridadRequest.builder()
                .nombre(nombre)
                .nivel(nivel)
                .descripcion(descripcion)
                .estado(estado)
                .build();

        return prioridadService.actualizar(id, request, imagen);
    }

    // =====================================================
    // LISTAR PRIORIDADES - ADMIN/USER
    // =====================================================
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public List<PrioridadResponse> listarPrioridades() {
        return prioridadService.listar();
    }

    // =====================================================
    // OBTENER POR ID - ADMIN/USER
    // =====================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public PrioridadResponse obtenerPorId(@PathVariable Long id) {
        return prioridadService.obtenerPorId(id);
    }

    // =====================================================
    // ELIMINAR - SOLO ADMIN
    // =====================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminarPrioridad(@PathVariable Long id) {
        prioridadService.eliminar(id);
    }
}