package Back_Goblink_park.demo.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarPrioridadReporteRequest {

    // =====================================================
    // PRIORIDAD
    // =====================================================

    @NotNull(message = "La prioridad es obligatoria")
    private Long prioridadId;

}