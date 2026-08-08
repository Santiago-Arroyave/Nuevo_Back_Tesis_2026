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
    // 1. CONSULTAS BÁSICAS
    // ==========================================
    List<ProyectoMiembro> findByProyectoId(Long proyectoId);

    List<ProyectoMiembro> findByProyectoIdAndEstadoTrue(Long proyectoId);

    List<ProyectoMiembro> findByUsuarioId(Long usuarioId);

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
    // 3. PAGINACIÓN
    // ==========================================
    Page<ProyectoMiembro> findByProyectoId(Long proyectoId, Pageable pageable);

    Page<ProyectoMiembro> findByUsuarioId(Long usuarioId, Pageable pageable);

    // ==========================================
    // 4. CONSULTAS PERSONALIZADAS (JOIN FETCH)
    // ==========================================
    @Query("SELECT pm FROM ProyectoMiembro pm " +
            "JOIN FETCH pm.usuario u " +
            "WHERE pm.proyecto.id = :proyectoId AND pm.estado = true")
    List<ProyectoMiembro> findActiveMembersWithUserByProyectoId(@Param("proyectoId") Long proyectoId);
}