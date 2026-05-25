package Back_Goblink_park.demo.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsableProyectoResponse {

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
    // USUARIO RESPONSABLE
    // =====================================================

    private Long usuarioResponsableId;

    private String usuarioResponsableNombre;

    // =====================================================
    // INFORMACIÓN
    // =====================================================

    private String titulo;

    private String descripcion;

    // =====================================================
    // ESTADO
    // =====================================================

    private String estado;

    // =====================================================
    // PRIORIDAD
    // =====================================================

    private String prioridad;

    // =====================================================
    // FECHAS
    // =====================================================

    private LocalDate fechaInicio;

    private LocalDate fechaLimite;

    // =====================================================
    // AVANCE
    // =====================================================

    private BigDecimal porcentajeAvance;

    // =====================================================
    // AUDITORÍA
    // =====================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}