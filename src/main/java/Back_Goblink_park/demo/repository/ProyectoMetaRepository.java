package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.ProyectoMeta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProyectoMetaRepository
        extends JpaRepository<ProyectoMeta, Long> {

    // =====================================================
    // LISTAR METAS POR PROYECTO
    // =====================================================

    List<ProyectoMeta> findByProyectoId(
            Long proyectoId
    );
}