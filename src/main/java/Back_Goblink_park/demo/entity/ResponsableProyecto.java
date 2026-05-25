package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "responsables_proyecto")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsableProyecto {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_responsable")

    private Long id;

    // =====================================================
    // RELACIÓN PROYECTO
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_proyecto")

    private Proyecto proyecto;

    // =====================================================
    // RELACIÓN USUARIO RESPONSABLE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_usuario_responsable")

    private Usuario usuarioResponsable;

    // =====================================================
    // CRONOGRAMA DE ACTIVIDADES PROYECTO
    // =====================================================
    @OneToMany(
            mappedBy = "responsable",
            cascade = CascadeType.ALL
    )
    private List<CronogramaActividadProyecto>
            actividades;

    // =====================================================
    // INFORMACIÓN
    // =====================================================


    @Column(nullable = false, length = 150)

    private String titulo;

    @Column(columnDefinition = "TEXT")

    private String descripcion;

    // =====================================================
    // ESTADO
    // =====================================================

    /*
        pendiente
        en_progreso
        completada
        cancelada
     */

    @Column(nullable = false, length = 30)

    private String estado;

    // =====================================================
    // PRIORIDAD
    // =====================================================

    /*
        baja
        media
        alta
     */

    @Column(nullable = false, length = 20)

    private String prioridad;

    // =====================================================
    // FECHAS
    // =====================================================

    private LocalDate fechaInicio;

    private LocalDate fechaLimite;

    // =====================================================
    // AVANCE
    // =====================================================

    @Column(precision = 5, scale = 2)

    private BigDecimal porcentajeAvance;

    // =====================================================
    // AUDITORÍA
    // =====================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // =====================================================
    // PRE PERSIST
    // =====================================================

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();

        if (this.estado == null) {

            this.estado = "pendiente";
        }

        if (this.prioridad == null) {

            this.prioridad = "media";
        }

        if (this.porcentajeAvance == null) {

            this.porcentajeAvance = BigDecimal.ZERO;
        }
    }

    // =====================================================
    // PRE UPDATE
    // =====================================================

    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}