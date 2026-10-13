package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "reportes")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reporte {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long id;

    // =====================================================
    // RELACIONES
    // =====================================================

    // =====================================================
    // PROYECTOS ASOCIADOS
    // =====================================================

    @org.hibernate.annotations.BatchSize(size = 50)
    @OneToMany(
            mappedBy = "reporte",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProyectoReporte> proyectosAsociados;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prioridad")
    private Prioridad prioridad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_reporte")
    private EstadoReporte estadoReporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validado_por")
    private Usuario validadoPor;

    // =====================================================
    // INFORMACIÓN
    // =====================================================

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private LocalDateTime fechaEvento;

    private LocalDateTime fechaReporte;

    private String fuente;

    // =====================================================
    // GEOLOCALIZACIÓN
    // =====================================================

    private Double latitud;

    private Double longitud;

    private String direccionReferencia;

    @Column(name = "sector")
    private String sector;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "datos_especificos", columnDefinition = "jsonb")
    private Map<String, Object> datosEspecificos;

    // =====================================================
    // EVIDENCIA
    // =====================================================

    @Column(name = "foto_principal_url", length = 1000)
    private String fotoPrincipalUrl;

    // =====================================================
    // VALIDACIÓN ADMIN
    // =====================================================

    private LocalDateTime fechaValidacion;

    @Column(columnDefinition = "TEXT")
    private String observacionesAdmin;

    // =====================================================
    // ESTADO
    // =====================================================

    private Boolean publico;

    private Boolean eliminado;

    private Boolean estado;

    // =====================================================
    // AUDITORÍA
    // =====================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // =====================================================
    // RELACIONES HIJAS
    // =====================================================

    @OneToMany(
            mappedBy = "reporte",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    private List<EvidenciaReporte> evidencias;

    @OneToMany(
            mappedBy = "reporte",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ComentarioReporte> comentarios;

    // =====================================================
    // PRE PERSIST
    // =====================================================

    @PrePersist
    public void prePersist() {

        this.fechaReporte = LocalDateTime.now();

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();

        this.publico = true;

        this.eliminado = false;

        this.estado = true;

        if (this.fuente == null) {

            this.fuente = "mobile";
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