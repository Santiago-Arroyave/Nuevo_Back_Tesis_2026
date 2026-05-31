package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long>,
        JpaSpecificationExecutor<Usuario> {

    // =====================================================
    // BUSCAR POR CORREO
    // =====================================================

    Optional<Usuario> findByCorreo(
            String correo
    );

    // =====================================================
    // VALIDAR CORREO
    // =====================================================

    boolean existsByCorreo(
            String correo
    );

    // =====================================================
    // VALIDAR USERNAME
    // =====================================================

    boolean existsByUsername(
            String username
    );

    // =====================================================
    // LISTAR ACTIVOS
    // =====================================================

    List<Usuario> findByEstadoTrue();

    // =====================================================
    // LISTAR POR ROL
    // =====================================================

    List<Usuario> findByRolId(
            Long rolId
    );

    // =====================================================
    // BUSCAR POR NOMBRE
    // =====================================================

    List<Usuario> findByNombresContainingIgnoreCase(
            String nombres
    );

    // =====================================================
    // CONTAR USUARIOS ACTIVOS
    // =====================================================

    Long countByEstadoTrue();

    // =====================================================
    // CONTAR POR ROL
    // =====================================================

    Long countByRolId(
            Long rolId
    );
}