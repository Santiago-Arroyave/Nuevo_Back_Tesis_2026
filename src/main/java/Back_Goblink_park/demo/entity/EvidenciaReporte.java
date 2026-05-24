package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "evidencias_reporte")

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvidenciaReporte {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evidencia")
    private Long id;

    // =====================================================
    // RELACIÓN REPORTE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_reporte",
            nullable = false
    )
    private Reporte reporte;

    // =====================================================
    // DATOS ARCHIVO
    // =====================================================

    @Column(name = "tipo_archivo", nullable = false)
    private String tipoArchivo;

    @Column(name = "url_archivo", nullable = false)
    private String urlArchivo;

    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @Column(name = "tamano_archivo")
    private Long tamanoArchivo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "es_principal")
    private Boolean esPrincipal;

    // =====================================================
    // NUEVAS COLUMNAS
    // =====================================================

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "storage_provider")
    private String storageProvider;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    // =====================================================
    // FECHAS
    // =====================================================

    @Column(name = "fecha_carga")
    private LocalDateTime fechaCarga;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =====================================================
    // PRE PERSIST
    // =====================================================

    @PrePersist
    public void prePersist() {

        this.fechaCarga = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.esPrincipal == null) {
            this.esPrincipal = false;
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