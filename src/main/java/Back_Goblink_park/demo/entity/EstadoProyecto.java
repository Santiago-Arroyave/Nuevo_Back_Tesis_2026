package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "estados_proyecto")

@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor

public class EstadoProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_estado_proyecto")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(length = 150)
    private String descripcion;

    @Column(length = 20)
    private String color;

    @Column(name = "orden_visual", nullable = false)
    private Short ordenVisual;

    @Builder.Default
    private Boolean estado = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =====================================================
    // TIMESTAMPS
    // =====================================================

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}