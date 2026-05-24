package Back_Goblink_park.demo.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioReporteResponse {

    private Long id;

    // =========================================
    // REPORTE
    // =========================================

    private Long reporteId;

    // =========================================
    // USUARIO
    // =========================================

    private Long usuarioId;

    private String usuarioNombre;

    private String usuarioCorreo;

    // =========================================
    // COMENTARIO
    // =========================================

    private String comentario;

    private String tipoComentario;

    private Boolean editado;

    // =========================================
    // FECHAS
    // =========================================

    private LocalDateTime fechaComentario;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}