package Back_Goblink_park.demo.controller;

import Back_Goblink_park.demo.dto.request.PrioridadRequest;
import Back_Goblink_park.demo.dto.response.PrioridadResponse;
import Back_Goblink_park.demo.service.interfaces.PrioridadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Prioridades", description = "Endpoints para la gestión de prioridades")
@SecurityRequirement(name = "bearerAuth") // Asumiendo que usas JWT. Si usas otro, ajústalo o quítalo.
public class PrioridadController {

    private final PrioridadService prioridadService;

    // =====================================================
    // CREAR PRIORIDAD - SOLO ADMIN
    // =====================================================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Crear prioridad", description = "Crea una nueva prioridad. Solo accesible para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prioridad creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación o archivo inválido"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    public PrioridadResponse crearPrioridad(
            @Parameter(description = "Nombre (máx. 20 caracteres)", required = true)
            @RequestParam("nombre") @NotBlank @Size(max = 20) String nombre,

            @Parameter(description = "Nivel (1 a 4)", required = true)
            @RequestParam("nivel") @NotNull @Min(1) @Max(4) Integer nivel,

            @Parameter(description = "Descripción (máx. 100 caracteres)")
            @RequestParam(value = "descripcion", required = false) @Size(max = 100) String descripcion,

            @Parameter(description = "Estado (true/false)")
            @RequestParam(value = "estado", defaultValue = "true") Boolean estado,

            @Parameter(description = "Imagen (opcional)", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) {
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
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // <-- Ojo: Se agregó consumes
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Actualizar prioridad", description = "Actualiza una prioridad existente por su ID. Solo accesible para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prioridad actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "404", description = "Prioridad no encontrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    public PrioridadResponse actualizarPrioridad(
            @Parameter(description = "ID de la prioridad", required = true)
            @PathVariable Long id,

            @Parameter(description = "Nombre (máx. 20 caracteres)", required = true)
            @RequestParam("nombre") @NotBlank @Size(max = 20) String nombre,

            @Parameter(description = "Nivel (1 a 4)", required = true)
            @RequestParam("nivel") @NotNull @Min(1) @Max(4) Integer nivel,

            @Parameter(description = "Descripción (máx. 100 caracteres)")
            @RequestParam(value = "descripcion", required = false) @Size(max = 100) String descripcion,

            @Parameter(description = "Estado (true/false)")
            @RequestParam(value = "estado", defaultValue = "true") Boolean estado,

            @Parameter(description = "Nueva imagen (opcional)", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
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
    @Operation(summary = "Listar prioridades", description = "Obtiene la lista de todas las prioridades.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    public List<PrioridadResponse> listarPrioridades() {
        return prioridadService.listar();
    }

    // =====================================================
    // OBTENER POR ID - ADMIN/USER
    // =====================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Obtener por ID", description = "Obtiene los detalles de una prioridad por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prioridad encontrada"),
            @ApiResponse(responseCode = "404", description = "Prioridad no encontrada")
    })
    public PrioridadResponse obtenerPorId(
            @Parameter(description = "ID de la prioridad", required = true)
            @PathVariable Long id
    ) {
        return prioridadService.obtenerPorId(id);
    }

    // =====================================================
    // ELIMINAR - SOLO ADMIN
    // =====================================================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // <-- Estándar REST para eliminación exitosa (204 No Content)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Eliminar prioridad", description = "Elimina una prioridad por su ID. Solo accesible para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Prioridad eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Prioridad no encontrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    public void eliminarPrioridad(
            @Parameter(description = "ID de la prioridad", required = true)
            @PathVariable Long id
    ) {
        prioridadService.eliminar(id);
    }
}