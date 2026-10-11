package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long>,
        JpaSpecificationExecutor<Usuario> {

    // =====================================================
    // BUSCAR POR CORREO
    // =====================================================
    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    // =====================================================
    // BUSCAR POR USERNAME ✅ AGREGADO (Faltaba este método)
    // =====================================================
    Optional<Usuario> findByUsername(String username);

    // =====================================================
    // VALIDAR CORREO
    // =====================================================
    boolean existsByCorreo(String correo);

    boolean existsByCorreoIgnoreCase(String correo);

    // =====================================================
    // VALIDAR USERNAME
    // =====================================================
    boolean existsByUsername(String username);

    // =====================================================
    // LISTAR ACTIVOS
    // =====================================================
    List<Usuario> findByEstadoTrue();

    // =====================================================
    // LISTAR POR ROL
    // =====================================================
    List<Usuario> findByRolId(Long rolId);

    // =====================================================
    // BUSCAR POR NOMBRE
    // =====================================================
    List<Usuario> findByNombresContainingIgnoreCase(String nombres);

    // =====================================================
    // CONTAR USUARIOS ACTIVOS
    // =====================================================
    Long countByEstadoTrue();

    // =====================================================
    // CONTAR POR ROL
    // =====================================================
    Long countByRolId(Long rolId);

    // =====================================================
    // OBTENER ESTADÍSTICAS DEL USUARIO
    // =====================================================
    @Query(value = "SELECT " +
           "(SELECT COUNT(*) FROM public.proyecto_miembros WHERE id_usuario = :idUsuario AND estado = true) AS totalproyectos, " +
           "(SELECT COUNT(*) FROM public.reportes WHERE id_usuario = :idUsuario AND eliminado = false) AS totalreportes, " +
           "(SELECT COUNT(*) FROM public.evidencias_reporte er " +
           "   JOIN public.reportes r ON er.id_reporte = r.id_reporte " +
           " WHERE r.id_usuario = :idUsuario) AS totalevidencias", 
           nativeQuery = true)
    Map<String, Object> obtenerEstadisticasUsuarioRaw(@Param("idUsuario") Long idUsuario);
}