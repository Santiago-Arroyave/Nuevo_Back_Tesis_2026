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
public class ProyectoResponse {

    // =====================================================
    // ID
    // =====================================================

    private Long id;

    // =====================================================
    // ESTADO PROYECTO
    // =====================================================

    private Long estadoProyectoId;

    private String estadoProyectoNombre;

    // =====================================================
    // PRIORIDAD
    // =====================================================

    private Long prioridadId;

    private String prioridadNombre;

    // =====================================================
    // CATEGORÍA
    // =====================================================

    private Long categoriaId;

    private String categoriaNombre;

    // =====================================================
    // CREADO POR
    // =====================================================

    private Long creadoPorId;

    private String creadoPorNombre;

    // =====================================================
    // INFORMACIÓN
    // =====================================================

    private String nombre;

    private String descripcion;

    private BigDecimal presupuestoEstimado;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String observaciones;

    // =====================================================
    // UBICACIÓN
    // =====================================================

    private Double latitud;

    private Double longitud;

    private String ubicacion;

    // =====================================================
    // EXTRA
    // =====================================================

    private String tipoProyecto;

    private Double porcentajeAvance;

    private Boolean estado;

    private Boolean eliminado;

    // =====================================================
    // FECHAS
    // =====================================================

    private LocalDateTime fechaCreacion;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}