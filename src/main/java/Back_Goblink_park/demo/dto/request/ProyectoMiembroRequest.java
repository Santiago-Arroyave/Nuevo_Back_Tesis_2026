package Back_Goblink_park.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoMiembroRequest {

    @NotNull(message = "El ID del proyecto es obligatorio")
    private Long proyectoId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El rol del miembro en el proyecto es obligatorio")
    private String rolEnProyecto;

    private Boolean estado; // Opcional, por defecto se puede asumir true
}