package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.EstadoReporte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoReporteRepository
        extends JpaRepository<EstadoReporte, Long> {

    // =====================================================
    // VALIDAR EXISTENCIA POR NOMBRE
    // =====================================================

    boolean existsByNombre(
            String nombre
    );

    // =====================================================
    // BUSCAR POR NOMBRE IGNORANDO MAYÚSCULAS / MINÚSCULAS
    // =====================================================

    Optional<EstadoReporte> findByNombreIgnoreCase(
            String nombre
    );
}