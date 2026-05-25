package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "proyecto_miembros",

        uniqueConstraints = {

                @UniqueConstraint(
                        columnNames = {
                                "id_proyecto",
                                "id_usuario"
                        }
                )
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoMiembro {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_proyecto_miembro")

    private Long id;

    // =====================================================
    // RELACIÓN PROYECTO
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(
            name = "id_proyecto",
            nullable = false
    )

    private Proyecto proyecto;

    // =====================================================
    // RELACIÓN USUARIO
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(
            name = "id_usuario",
            nullable = false
    )

    private Usuario usuario;

    // =====================================================
    // ROL EN PROYECTO
    // =====================================================

    /*
        ADMIN
        COORDINADOR
        INVESTIGADOR
        VOLUNTARIO
        APOYO
    */

    @Column(name = "rol_en_proyecto")

    private String rolEnProyecto;

    // =====================================================
    // ESTADO
    // =====================================================

    private Boolean estado;

    // =====================================================
    // FECHAS
    // =====================================================

    private LocalDateTime fechaAsignacion;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // =====================================================
    // PRE PERSIST
    // =====================================================

    @PrePersist
    public void prePersist() {

        this.fechaAsignacion =
                LocalDateTime.now();

        this.createdAt =
                LocalDateTime.now();

        this.updatedAt =
                LocalDateTime.now();

        if (this.estado == null) {

            this.estado = true;
        }
    }

    // =====================================================
    // PRE UPDATE
    // =====================================================

    @PreUpdate
    public void preUpdate() {

        this.updatedAt =
                LocalDateTime.now();
    }
}