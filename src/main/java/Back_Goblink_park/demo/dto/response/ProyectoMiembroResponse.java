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
    private String usuarioUsername; // Agregado por consistencia si lo necesitas en front

    // =====================================================
    // ROL EN PROYECTO
    // =====================================================
    private String rolEnProyecto;

    // =====================================================
    // ESTADO DEL MIEMBRO
    // =====================================================
    private Boolean estado;

    // =====================================================
    // FECHAS
    // =====================================================
    private LocalDateTime fechaAsignacion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}