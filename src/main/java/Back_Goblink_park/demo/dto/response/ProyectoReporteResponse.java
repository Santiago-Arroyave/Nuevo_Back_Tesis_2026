package Back_Goblink_park.demo.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoReporteResponse {

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
    // REPORTE
    // =====================================================

    private Long reporteId;

    private String reporteTitulo;

    private String reporteDescripcion;

    // =====================================================
    // ESTADO REPORTE
    // =====================================================

    private String estadoReporte;

    // =====================================================
    // PRIORIDAD
    // =====================================================

    private String prioridad;

    // =====================================================
    // FECHA ASOCIACIÓN
    // =====================================================

    private LocalDateTime fechaAsociacion;

    // =====================================================
    // AUDITORÍA
    // =====================================================

    private LocalDateTime createdAt;
}