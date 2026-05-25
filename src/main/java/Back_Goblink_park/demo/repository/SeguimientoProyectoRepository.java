package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.SeguimientoProyecto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeguimientoProyectoRepository
        extends JpaRepository<SeguimientoProyecto, Long> {

    // =====================================================
    // LISTAR POR PROYECTO
    // =====================================================

    List<SeguimientoProyecto>
    findByProyectoIdOrderByFechaSeguimientoDesc(
            Long proyectoId
    );

    // =====================================================
    // LISTAR POR USUARIO
    // =====================================================

    List<SeguimientoProyecto>
    findByUsuarioIdOrderByFechaSeguimientoDesc(
            Long usuarioId
    );

    // =====================================================
    // LISTAR POR PROYECTO Y USUARIO
    // =====================================================

    List<SeguimientoProyecto>
    findByProyectoIdAndUsuarioId(
            Long proyectoId,
            Long usuarioId
    );

    // =====================================================
    // ÚLTIMOS SEGUIMIENTOS
    // =====================================================

    List<SeguimientoProyecto>
    findTop10ByOrderByFechaSeguimientoDesc();
}