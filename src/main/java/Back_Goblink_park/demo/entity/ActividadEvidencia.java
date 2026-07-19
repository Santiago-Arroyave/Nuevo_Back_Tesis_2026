package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "actividad_evidencias")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActividadEvidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evidencia_actividad")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_actividad", nullable = false)
    private CronogramaActividadProyecto actividad;

    @Column(columnDefinition = "TEXT", name = "imagen_base64")
    private String imagenBase64;

    @Column(length = 50, name = "tipo_imagen")
    private String tipoImagen;

    @Column(name = "nombre_archivo")
    private String nombreArchivo;

    @Column(name = "tamano_archivo")
    private Long tamanoArchivo;

    @Column(length = 100, name = "mime_type")
    private String mimeType;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "es_principal")
    private Boolean esPrincipal;

    @Column(name = "fecha_carga")
    private LocalDateTime fechaCarga;

    @PrePersist
    protected void onCreate() {
        this.fechaCarga = LocalDateTime.now();
    }
}