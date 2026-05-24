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
    // RELACIÓN CON ROL
    // =====================================================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    // =====================================================
    // DATOS PERSONALES
    // =====================================================

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, unique = true, length = 120)
    private String correo;

    @Column(unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(length = 20)
    private String telefono;

    @Column(name = "foto_perfil")
    private String fotoPerfil;

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