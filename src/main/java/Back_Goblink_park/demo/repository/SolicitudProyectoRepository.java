package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.SolicitudProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudProyectoRepository extends JpaRepository<SolicitudProyecto, Long> {

    List<SolicitudProyecto> findByProyectoIdAndEstadoOrderByFechaSolicitudDesc(Long proyectoId, String estado);

    List<SolicitudProyecto> findByProyectoIdOrderByFechaSolicitudDesc(Long proyectoId);

    List<SolicitudProyecto> findByEstadoOrderByFechaSolicitudDesc(String estado);

    Optional<SolicitudProyecto> findByUsuarioIdAndProyectoIdAndEstado(Long usuarioId, Long proyectoId, String estado);

    List<SolicitudProyecto> findByUsuarioIdOrderByFechaSolicitudDesc(Long usuarioId);

}