package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "proyecto_presupuesto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoPresupuesto {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "id_presupuesto")
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
    // RUBRO
    // =====================================================

    @Column(
            nullable = false,
            length = 150
    )
    private String rubro;

    // =====================================================
    // MONTO
    // =====================================================

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal monto;

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

        if (this.monto == null) {

            this.monto = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}