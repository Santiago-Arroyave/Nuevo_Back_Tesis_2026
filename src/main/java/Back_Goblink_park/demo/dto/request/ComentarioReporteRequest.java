package Back_Goblink_park.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioReporteRequest {

    @NotNull(message = "El reporte es obligatorio")
    private Long reporteId;

    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;

    private String tipoComentario;
}