package Back_Goblink_park.demo.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoRequest {

    // =====================================================
    // RELACIONES
    // =====================================================

    private Long estadoProyectoId;

    private Long prioridadId;

    private Long categoriaId;

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
}