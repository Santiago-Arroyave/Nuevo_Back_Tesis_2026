package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private long id;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 150)
    private String descripcion;

    @Column(nullable = false)
    private Boolean estado = true;

    @Column(length = 7)
    private String color;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "imagen_base64", columnDefinition = "TEXT")
    private String imagenBase64;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =====================================================
    // REPORTES
    // =====================================================
    @OneToMany(mappedBy = "categoria")
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