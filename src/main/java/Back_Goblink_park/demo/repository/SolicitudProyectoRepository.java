package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.SolicitudProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudProyectoRepository extends JpaRepository<SolicitudProyecto, Long> {

    // Solicitudes pendientes de un proyecto (para el admin)
    List<SolicitudProyecto> findByProyectoIdAndEstadoOrderByFechaSolicitudDesc(
            Long proyectoId, String estado);

    // Todas las solicitudes de un proyecto
    List<SolicitudProyecto> findByProyectoIdOrderByFechaSolicitudDesc(Long proyectoId);

    // Todas las solicitudes pendientes (dashboard admin)
    List<SolicitudProyecto> findByEstadoOrderByFechaSolicitudDesc(String estado);

    // Verificar si ya existe una solicitud pendiente
    Optional<SolicitudProyecto> findByUsuarioIdAndProyectoIdAndEstado(
            Long usuarioId, Long proyectoId, String estado);

    // Solicitudes de un usuario
    List<SolicitudProyecto> findByUsuarioIdOrderByFechaSolicitudDesc(Long usuarioId);

    // Contar solicitudes pendientes de un proyecto
    long countByProyectoIdAndEstado(Long proyectoId, String estado);

    // Verificar si el usuario ya es miembro del proyecto
    @Query("SELECT COUNT(pm) > 0 FROM ProyectoMiembro pm " +
            "WHERE pm.usuario.id = :usuarioId AND pm.proyecto.id = :proyectoId AND pm.estado = true")
    boolean existeMiembroActivo(@Param("usuarioId") Long usuarioId,
                                @Param("proyectoId") Long proyectoId);
}