package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table (name = "prioridades")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Prioridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_prioridad")
    private long id;

    @Column (name = "nombre", unique = true, length = 20)
    private String nombre;

    @Column(nullable = false)
    private Short nivel;

    @Column(length = 100)
    private String descripcion;

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
            mappedBy = "prioridad"
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
