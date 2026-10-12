package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.ProyectoMiembro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoMiembroRepository extends JpaRepository<ProyectoMiembro, Long> {

    // ==========================================
    // 1. CONSULTAS BÁSICAS (OPTIMIZADAS CON JOIN FETCH)
    // ==========================================
    @Query("SELECT pm FROM ProyectoMiembro pm " +
           "JOIN FETCH pm.proyecto p " +
           "LEFT JOIN FETCH p.estadoProyecto " +
           "LEFT JOIN FETCH p.prioridad " +
           "LEFT JOIN FETCH p.categoria " +
           "JOIN FETCH pm.usuario u " +
           "WHERE pm.proyecto.id = :proyectoId")
    List<ProyectoMiembro> findByProyectoId(@Param("proyectoId") Long proyectoId);

    @Query("SELECT pm FROM ProyectoMiembro pm " +
           "JOIN FETCH pm.proyecto p " +
           "LEFT JOIN FETCH p.estadoProyecto " +
           "LEFT JOIN FETCH p.prioridad " +
           "LEFT JOIN FETCH p.categoria " +
           "JOIN FETCH pm.usuario u " +
           "WHERE pm.proyecto.id = :proyectoId AND pm.estado = true")
    List<ProyectoMiembro> findByProyectoIdAndEstadoTrue(@Param("proyectoId") Long proyectoId);

    @Query("SELECT pm FROM ProyectoMiembro pm " +
           "JOIN FETCH pm.proyecto p " +
           "LEFT JOIN FETCH p.estadoProyecto " +
           "LEFT JOIN FETCH p.prioridad " +
           "LEFT JOIN FETCH p.categoria " +
           "JOIN FETCH pm.usuario u " +
           "WHERE pm.usuario.id = :usuarioId")
    List<ProyectoMiembro> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT pm FROM ProyectoMiembro pm " +
           "JOIN FETCH pm.proyecto p " +
           "LEFT JOIN FETCH p.estadoProyecto " +
           "LEFT JOIN FETCH p.prioridad " +
           "LEFT JOIN FETCH p.categoria " +
           "JOIN FETCH pm.usuario u " +
           "WHERE pm.usuario.id = :usuarioId AND pm.estado = true")
    List<ProyectoMiembro> findByUsuarioIdAndEstadoTrue(@Param("usuarioId") Long usuarioId);

    Optional<ProyectoMiembro> findByProyectoIdAndUsuarioId(Long proyectoId, Long usuarioId);

    // ==========================================
    // 2. VALIDACIÓN DE EXISTENCIA
    // ==========================================
    boolean existsByProyectoIdAndUsuarioId(Long proyectoId, Long usuarioId);

    boolean existsByUsuarioIdAndProyectoIdAndEstado(Long usuarioId, Long proyectoId, boolean estado);

    // Método semántico para verificar si es miembro activo (reutiliza el método de arriba)
    default boolean existeMiembroActivo(Long usuarioId, Long proyectoId) {
        return existsByUsuarioIdAndProyectoIdAndEstado(usuarioId, proyectoId, true);
    }

    Long countByEstadoTrue();

    Long countByProyectoIdAndEstadoTrue(Long proyectoId);

    // ==========================================
    // 3. PAGINACIÓN (OPTIMIZADA)
    // ==========================================
    @Query(value = "SELECT pm FROM ProyectoMiembro pm " +
           "JOIN FETCH pm.proyecto p " +
           "LEFT JOIN FETCH p.estadoProyecto " +
           "LEFT JOIN FETCH p.prioridad " +
           "LEFT JOIN FETCH p.categoria " +
           "JOIN FETCH pm.usuario u " +
           "WHERE pm.proyecto.id = :proyectoId",
           countQuery = "SELECT COUNT(pm) FROM ProyectoMiembro pm WHERE pm.proyecto.id = :proyectoId")
    Page<ProyectoMiembro> findByProyectoId(@Param("proyectoId") Long proyectoId, Pageable pageable);

    @Query(value = "SELECT pm FROM ProyectoMiembro pm " +
           "JOIN FETCH pm.proyecto p " +
           "LEFT JOIN FETCH p.estadoProyecto " +
           "LEFT JOIN FETCH p.prioridad " +
           "LEFT JOIN FETCH p.categoria " +
           "JOIN FETCH pm.usuario u " +
           "WHERE pm.usuario.id = :usuarioId",
           countQuery = "SELECT COUNT(pm) FROM ProyectoMiembro pm WHERE pm.usuario.id = :usuarioId")
    Page<ProyectoMiembro> findByUsuarioId(@Param("usuarioId") Long usuarioId, Pageable pageable);

    // ==========================================
    // 4. CONSULTAS PERSONALIZADAS (JOIN FETCH)
    // ==========================================
    @Query("SELECT pm FROM ProyectoMiembro pm " +
            "JOIN FETCH pm.usuario u " +
            "WHERE pm.proyecto.id = :proyectoId AND pm.estado = true")
    List<ProyectoMiembro> findActiveMembersWithUserByProyectoId(@Param("proyectoId") Long proyectoId);
}