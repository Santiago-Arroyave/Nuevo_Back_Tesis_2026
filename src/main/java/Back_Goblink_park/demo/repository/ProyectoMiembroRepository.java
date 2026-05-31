package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.ProyectoMiembro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

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


    Page<ProyectoMiembro> findByProyectoId(Long proyectoId, Pageable pageable);

    Page<ProyectoMiembro> findByUsuarioId(Long usuarioId, Pageable pageable);

}