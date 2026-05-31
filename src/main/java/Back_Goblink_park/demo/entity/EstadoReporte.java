package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "estados_reporte")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EstadoReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_reporte")
    private long id;

    @Column(name = "nombre", length = 50, unique = true, nullable = false)
    private String nombre;

    @Column(name = "descripcion",length = 150)
    private String descripcion;

    @Column(length = 7)
    private String color;

    @Column(name = "orden_visual", nullable = false)
    private Short ordenVisual;

    @Column(nullable = false)
    private Boolean estado;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =====================================================
    // REPORTES
    // =====================================================

    @OneToMany(
            mappedBy = "estadoReporte"
    )
    private List<Reporte> reportes;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (estado == null) {
            estado = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
