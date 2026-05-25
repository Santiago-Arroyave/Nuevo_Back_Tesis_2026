package Back_Goblink_park.demo.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoMiembroResponse {

    // =====================================================
    // ID
    // =====================================================

    private Long id;

    // =====================================================
    // PROYECTO
    // =====================================================

    private Long proyectoId;

    private String proyectoNombre;

    // =====================================================
    // USUARIO
    // =====================================================

    private Long usuarioId;

    private String usuarioNombre;

    private String usuarioCorreo;

    // =====================================================
    // ROL
    // =====================================================

    private String rolEnProyecto;

    // =====================================================
    // ESTADO
    // =====================================================

    private Boolean estado;

    // =====================================================
    // FECHAS
    // =====================================================

    private LocalDateTime fechaAsignacion;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}