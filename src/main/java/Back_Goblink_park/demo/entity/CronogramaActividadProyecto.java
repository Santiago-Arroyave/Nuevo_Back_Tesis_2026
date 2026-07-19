package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cronograma_actividades_proyecto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaActividadProyecto {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad")
    private Long id;

    // =====================================================
    // PROYECTO
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto", nullable = true)
    private Proyecto proyecto;

    // =====================================================
    // RESPONSABLE (AHORA APUNTA A PROYECTO_MIEMBRO)
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto_miembro", nullable = false)
    private ProyectoMiembro proyectoMiembro;

    // =====================================================
    // INFORMACIÓN
    // =====================================================

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // =====================================================
    // ESTADO
    // =====================================================

    @Column(nullable = false, length = 30)
    private String estado;

    // =====================================================
    // PRIORIDAD
    // =====================================================

    @Column(nullable = false, length = 20)
    private String prioridad;

    // =====================================================
    // FECHAS
    // =====================================================

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    // =====================================================
    // EVIDENCIA - URL (OPCIONAL - si se sube a servidor)
    // =====================================================

    @Column(columnDefinition = "TEXT")
    private String urlEvidencia;

    // =====================================================
    // EVIDENCIA - IMAGEN BASE64 (NUEVO CAMPO)
    // =====================================================

    @Column(columnDefinition = "TEXT", name = "imagen_base64")
    private String imagenBase64;

    @Column(length = 50, name = "tipo_imagen")
    private String tipoImagen;  // "image/jpeg", "image/png", etc.

    // =====================================================
    // OBSERVACIONES
    // =====================================================

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // =====================================================
    // AVANCE
    // =====================================================

    @Column(precision = 5, scale = 2)
    private BigDecimal porcentajeAvance;

    // =====================================================
    // AUDITORÍA
    // =====================================================

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ActividadEvidencia> evidencias = new ArrayList<>();
    // =====================================================
    // CALLBACKS
    // =====================================================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.estado == null) {
            this.estado = "pendiente";
        }

        if (this.prioridad == null) {
            this.prioridad = "media";
        }

        if (this.porcentajeAvance == null) {
            this.porcentajeAvance = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}