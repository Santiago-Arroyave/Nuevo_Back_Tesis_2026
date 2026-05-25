package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "proyecto_metas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoMeta {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "id_meta")
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
    // DESCRIPCIÓN DE LA META
    // =====================================================

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String descripcion;

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