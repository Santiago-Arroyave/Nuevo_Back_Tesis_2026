package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.ProyectoMiembro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProyectoMiembroRepository
        extends JpaRepository<ProyectoMiembro, Long> {

    // =====================================================
    // LISTAR MIEMBROS POR PROYECTO
    // =====================================================

    List<ProyectoMiembro> findByProyectoId(
            Long proyectoId
    );

    // =====================================================
    // LISTAR MIEMBROS ACTIVOS POR PROYECTO
    // =====================================================

    List<ProyectoMiembro> findByProyectoIdAndEstadoTrue(
            Long proyectoId
    );

    // =====================================================
    // LISTAR PROYECTOS POR USUARIO
    // =====================================================

    List<ProyectoMiembro> findByUsuarioId(
            Long usuarioId
    );

    // =====================================================
    // VALIDAR SI YA EXISTE
    // =====================================================

    Optional<ProyectoMiembro>
    findByProyectoIdAndUsuarioId(
            Long proyectoId,
            Long usuarioId
    );

    Long countByEstadoTrue();

    // =====================================================
    // VALIDAR EXISTENCIA
    // =====================================================

    boolean existsByProyectoIdAndUsuarioId(
            Long proyectoId,
            Long usuarioId
    );

    // =====================================================
    // PAGINACIÓN
    // =====================================================

    Page<ProyectoMiembro> findByProyectoId(Long proyectoId, Pageable pageable);

    Page<ProyectoMiembro> findByUsuarioId(Long usuarioId, Pageable pageable);

    // =====================================================
    // CONSULTA PERSONALIZADA: MIEMBROS CON NOMBRE DE USUARIO
    // =====================================================

    @Query("SELECT pm FROM ProyectoMiembro pm " +
            "JOIN FETCH pm.usuario u " +
            "WHERE pm.proyecto.id = :proyectoId AND pm.estado = true")
    List<ProyectoMiembro>
    findActiveMembersWithUserByProyectoId(
            @Param("proyectoId") Long proyectoId
    );

    // =====================================================
    // CONTAR MIEMBROS ACTIVOS POR PROYECTO
    // =====================================================

    Long countByProyectoIdAndEstadoTrue(
            Long proyectoId
    );
}