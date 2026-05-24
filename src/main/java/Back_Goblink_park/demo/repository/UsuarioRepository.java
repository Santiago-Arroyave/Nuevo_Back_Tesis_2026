package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    // =========================================
    // BUSCAR POR CORREO
    // =========================================

    Optional<Usuario> findByCorreo(String correo);

    // =========================================
    // VALIDAR CORREO
    // =========================================

    boolean existsByCorreo(String correo);

    // =========================================
    // VALIDAR USERNAME
    // =========================================

    boolean existsByUsername(String username);
}