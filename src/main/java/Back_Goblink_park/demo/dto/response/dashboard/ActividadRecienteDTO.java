package Back_Goblink_park.demo.dto.response.dashboard;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ActividadRecienteDTO {
    private String tipo; // "REPORTE", "PROYECTO", "ESTADO"
    private String titulo;
    private String descripcion;
    private String usuarioNombre;
    private LocalDateTime fecha;
}