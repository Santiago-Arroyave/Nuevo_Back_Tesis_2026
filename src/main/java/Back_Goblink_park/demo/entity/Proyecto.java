package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "proyectos")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proyecto {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_proyecto")

    private Long id;

    // =====================================================
    // ESTADO PROYECTO
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_estado_proyecto")

    private EstadoProyecto estadoProyecto;

    // =====================================================
    // PRIORIDAD
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_prioridad")

    private Prioridad prioridad;

    // =====================================================
    // CREADO POR
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "creado_por")

    private Usuario creadoPor;

    // =====================================================
    // CATEGORÍA
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_categoria")

    private Categoria categoria;

    // =====================================================
    // INFORMACIÓN
    // =====================================================

    @Column(nullable = false, length = 150)

    private String nombre;

    @Column(nullable = false, columnDefinition = "TEXT")

    private String descripcion;

    private BigDecimal presupuestoEstimado;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Column(columnDefinition = "TEXT")

    private String observaciones;

    // =====================================================
    // UBICACIÓN
    // =====================================================

    private Double latitud;

    private Double longitud;

    private String ubicacion;

    // =====================================================
    // NUEVAS COLUMNAS RECOMENDADAS
    // =====================================================

    private String tipoProyecto;

    private Double porcentajeAvance;

    private Boolean estado;

    private Boolean eliminado;

    // =====================================================
    // FECHAS
    // =====================================================

    private LocalDateTime fechaCreacion;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // =====================================================
    // RELACIONES INVERSAS
    // =====================================================

    // -----------------------------------------------------
    // MIEMBROS
    // -----------------------------------------------------

    @OneToMany(
            mappedBy = "proyecto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private java.util.List<ProyectoMiembro> miembros;

    // -----------------------------------------------------
    // REPORTES ASOCIADOS
    // -----------------------------------------------------

    @OneToMany(
            mappedBy = "proyecto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private java.util.List<ProyectoReporte> reportes;

    // =====================================================
// REPORTES ASOCIADOS
// =====================================================

    @OneToMany(
            mappedBy = "proyecto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private List<ProyectoReporte> reportesAsociados;

    // -----------------------------------------------------
    // RESPONSABLES
    // -----------------------------------------------------

    @OneToMany(
            mappedBy = "proyecto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private java.util.List<ResponsableProyecto> responsables;

    // -----------------------------------------------------
    // SEGUIMIENTOS
    // -----------------------------------------------------

    @OneToMany(
            mappedBy = "proyecto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private java.util.List<SeguimientoProyecto> seguimientos;

    // -----------------------------------------------------
    // ACTIVIDADES
    // -----------------------------------------------------

    @OneToMany(
            mappedBy = "proyecto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private java.util.List<CronogramaActividadProyecto> actividades;

    // -----------------------------------------------------
    // OBJETIVOS
    // -----------------------------------------------------

    @OneToMany(
            mappedBy = "proyecto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private java.util.List<ProyectoObjetivo> objetivos;

    // -----------------------------------------------------
    // METAS
    // -----------------------------------------------------

    @OneToMany(
            mappedBy = "proyecto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private java.util.List<ProyectoMeta> metas;


    // -----------------------------------------------------
    // PRESUPUESTOS
    // -----------------------------------------------------

    @OneToMany(
            mappedBy = "proyecto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private java.util.List<ProyectoPresupuesto> presupuestos;

    // =====================================================
    // PRE PERSIST
    // =====================================================

    @PrePersist
    public void prePersist() {

        this.fechaCreacion = LocalDateTime.now();

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();

        if (this.estado == null) {

            this.estado = true;
        }

        if (this.eliminado == null) {

            this.eliminado = false;
        }

        if (this.porcentajeAvance == null) {

            this.porcentajeAvance = 0.0;
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