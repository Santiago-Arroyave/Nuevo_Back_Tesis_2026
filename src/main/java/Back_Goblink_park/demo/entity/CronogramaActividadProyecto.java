package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "cronograma_actividades_proyecto"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaActividadProyecto {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "id_actividad")
    private Long id;

    // =====================================================
    // PROYECTO
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_proyecto",
            nullable = false
    )
    private Proyecto proyecto;

    // =====================================================
    // RESPONSABLE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_responsable"
    )
    private ResponsableProyecto responsable;

    // =====================================================
    // INFORMACIÓN
    // =====================================================

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // =====================================================
    // ESTADO
    // =====================================================

    @Column(nullable = false, length = 30)
    private String estado;

    // =====================================================
    // FECHAS
    // =====================================================

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    // =====================================================
    // EVIDENCIA
    // =====================================================

    @Column(columnDefinition = "TEXT")
    private String urlEvidencia;

    // =====================================================
    // OBSERVACIONES
    // =====================================================

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // =====================================================
    // AUDITORÍA
    // =====================================================

    @Column(
            name = "created_at",
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =====================================================
    // CALLBACKS
    // =====================================================

    @PrePersist
    protected void onCreate() {

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}