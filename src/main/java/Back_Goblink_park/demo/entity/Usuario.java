package Back_Goblink_park.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "usuarios")   // nombre exacto de la tabla en Supabase
@Data                       // genera getters, setters, toString, etc.
@NoArgsConstructor          // constructor vacío (requerido por JPA)
@AllArgsConstructor         // constructor con todos los campos
@Builder                    // patrón builder para crear objetos fácilmente
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    // Relación ManyToOne: muchos usuarios pueden tener el mismo rol
    // FetchType.EAGER para que el rol se cargue inmediatamente (necesario para authorities)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "correo", nullable = false, unique = true, length = 120)
    private String correo;

    @Column(name = "username", unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false)  // mapea a password_hash
    private String password;   // almacena el hash (no texto plano)

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "foto_perfil", columnDefinition = "TEXT")
    private String fotoPerfil;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Métodos callback de JPA para establecer fechas automáticamente
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ========== MÉTODOS OBLIGATORIOS DE UserDetails ==========

    /**
     * Devuelve los roles/permisos del usuario.
     * Spring Security usa esto para la autorización (hasRole, hasAuthority).
     * Aquí le decimos que el usuario tiene un solo rol (el que está en la BD).
     * Nota: Spring Security por defecto espera el prefijo "ROLE_" para usar hasRole().
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Obtenemos el nombre del rol (ej: "USER" o "ADMIN")
        String roleName = rol.getNombre();
        // Si no tiene el prefijo "ROLE_", se lo agregamos
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }
        // SimpleGrantedAuthority es una implementación simple de GrantedAuthority
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    /**
     * Spring Security usará este metodo para obtener el "nombre de usuario"
     * con el que se autenticará. Usamos el correo electrónico porque es único.
     */
    @Override
    public String getUsername() {
        return this.correo;   // autenticamos con correo
    }

    /**
     * Indica si la cuenta no ha expirado. Siempre true en nuestro caso.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta no está bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales (contraseña) no han expirado.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado.
     * Usamos el campo "estado" de la BD.
     */
    @Override
    public boolean isEnabled() {
        return this.estado;
    }

    // Nota: getPassword() ya lo proporciona Lombok (por el @Data) mapeando al campo password.
    // Ese metodo devuelve el hash de la contraseña almacenado.
}