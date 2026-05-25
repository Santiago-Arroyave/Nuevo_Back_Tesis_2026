package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "seguimientos_proyecto")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeguimientoProyecto {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_seguimiento")

    private Long id;

    // =====================================================
    // RELACIÓN PROYECTO
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_proyecto")

    private Proyecto proyecto;

    // =====================================================
    // RELACIÓN USUARIO
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_usuario")

    private Usuario usuario;

    // =====================================================
    // AVANCE
    // =====================================================

    @Column(columnDefinition = "TEXT", nullable = false)

    private String descripcionAvance;

    // =====================================================
    // PORCENTAJE
    // =====================================================

    @Column(precision = 5, scale = 2)

    private BigDecimal porcentajeAvance;

    // =====================================================
    // FECHA SEGUIMIENTO
    // =====================================================

    private LocalDateTime fechaSeguimiento;

    // =====================================================
    // OBSERVACIONES
    // =====================================================

    @Column(columnDefinition = "TEXT")

    private String observaciones;

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

        this.fechaSeguimiento = LocalDateTime.now();

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();

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