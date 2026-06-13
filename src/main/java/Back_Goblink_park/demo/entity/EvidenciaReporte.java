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
    // RELACIONES
    // =====================================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reporte", nullable = false)
    private Reporte reporte;

    // =====================================================
    // ARCHIVO BINARIO (BYTEA - PostgreSQL)
    // =====================================================
    @Column(name = "archivo_binario", columnDefinition = "BYTEA")
    private byte[] archivoBinario;  // ✅ Array de bytes para almacenamiento eficiente

    // =====================================================
    // METADATOS DEL ARCHIVO
    // =====================================================
    @Column(name = "tipo_archivo", length = 30, nullable = false)
    private String tipoArchivo;  // "imagen", "video", "audio", "documento"

    @Column(name = "nombre_archivo", length = 150)
    private String nombreArchivo;

    @Column(name = "mime_type", length = 100)
    private String mimeType;  // "image/jpeg", "image/png", etc.

    @Column(name = "tamano_archivo")
    private Long tamanoArchivo;  // Tamaño original en bytes

    @Column(name = "descripcion", length = 200)  // ✅ Aumentado a 500 para descripciones largas
    private String descripcion;

    // =====================================================
    // MARCADORES
    // =====================================================
    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false;

    // =====================================================
    // FECHAS DE AUDITORÍA
    // =====================================================
    @Column(name = "fecha_carga", nullable = false)
    private LocalDateTime fechaCarga;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // =====================================================
    // CALLBACKS DE JPA
    // =====================================================
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaCarga = now;
        this.createdAt = now;
        this.updatedAt = now;
        if (this.esPrincipal == null) {
            this.esPrincipal = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}