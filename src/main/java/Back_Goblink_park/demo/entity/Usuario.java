package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    // =====================================================
    // ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;


    // =====================================================
    // DATOS PERSONALES
    // =====================================================

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(nullable = false, unique = true, length = 120)
    private String correo;

    @Column(unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(length = 20)
    private String telefono;

    @Column(name = "documento_identidad")
    private String documentoIdentidad;

    @Column(name = "entidad")
    private String entidad;

    @Column(name = "cargo_rol")
    private String cargoRol;

    @Column(name = "municipio_residencia")
    private String municipioResidencia;

    @Column(name = "sobre_mi", columnDefinition = "TEXT")
    private String sobreMi;

    @Column(name = "foto_perfil", length = 1000)
    private String fotoPerfil;

    @Column(name = "foto_portada", length = 1000)
    private String fotoPortada;

    // =====================================================
    // CONTROL
    // =====================================================

    @Column(nullable = false)
    private Boolean estado = true;

    // =====================================================
    // FECHAS
    // =====================================================

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =====================================================
    // RELACIÓN CON ROL
    // =====================================================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    // =====================================================
    // REPORTES CREADOS
    // =====================================================

    @OneToMany(
            mappedBy = "usuario"
    )
    private List<Reporte> reportes;

    // =====================================================
    // REPORTES VALIDADOS
    // =====================================================

    @OneToMany(
            mappedBy = "validadoPor"
    )
    private List<Reporte> reportesValidados;

    // =====================================================
    // COMENTARIOS
    // =====================================================

    @OneToMany(
            mappedBy = "usuario"
    )
    private List<ComentarioReporte> comentarios;

    // =====================================================
    // SEGUIMIENTOS REALIZADOS
    // =====================================================

    @OneToMany(
            mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SeguimientoProyecto> seguimientosProyecto;

    // =====================================================
    // PROYECTOS PARTICIPANDO
    // =====================================================

    @OneToMany(
            mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProyectoMiembro> proyectosParticipando;

    // =====================================================
    // MÉTODOS AUTOMÁTICOS
    // =====================================================

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        fechaRegistro = now;
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}