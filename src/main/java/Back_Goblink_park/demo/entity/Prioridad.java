package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prioridades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prioridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prioridad")
    private Long id;

    @Column(name = "nombre", length = 20, nullable = false, unique = true)
    private String nombre;

    @Column(name = "nivel", nullable = false)
    private Integer nivel; // SMALLINT en Postgres → Integer en Java

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(length = 7)
    private String color;

    @Column(name = "imagen_base64", columnDefinition = "TEXT")
    private String imagenBase64;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // =====================================================
    // RELACIONES
    // =====================================================
    @OneToMany(mappedBy = "prioridad", fetch = FetchType.LAZY)
    private List<Reporte> reportes;

    // =====================================================
    // FECHAS AUTOMÁTICAS
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