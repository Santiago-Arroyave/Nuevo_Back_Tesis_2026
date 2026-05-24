package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.EstadoReporte;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoReporteRepository
        extends JpaRepository<EstadoReporte, Long> {

    boolean existsByNombre(String nombre);
}