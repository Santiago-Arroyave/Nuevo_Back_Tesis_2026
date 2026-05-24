package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios_reporte")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioReporte {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Long id;

    // =====================================================
    // RELACIÓN REPORTE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_reporte")

    private Reporte reporte;

    // =====================================================
    // RELACIÓN USUARIO
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_usuario")

    private Usuario usuario;

    // =====================================================
    // INFORMACIÓN
    // =====================================================

    @Column(columnDefinition = "TEXT")

    private String comentario;

    // =====================================================
    // TIPOS
    // =====================================================

    /*
        ADMIN
        USUARIO
        SISTEMA
     */

    private String tipoComentario;

    // =====================================================
    // ESTADO
    // =====================================================

    private Boolean editado;

    // =====================================================
    // FECHAS
    // =====================================================

    private LocalDateTime fechaComentario;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // =====================================================
    // PRE PERSIST
    // =====================================================

    @PrePersist
    public void prePersist() {

        this.fechaComentario = LocalDateTime.now();

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();

        if (this.editado == null) {

            this.editado = false;
        }

        if (this.tipoComentario == null) {

            this.tipoComentario = "USUARIO";
        }
    }

    // =====================================================
    // PRE UPDATE
    // =====================================================

    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();

        this.editado = true;
    }
}