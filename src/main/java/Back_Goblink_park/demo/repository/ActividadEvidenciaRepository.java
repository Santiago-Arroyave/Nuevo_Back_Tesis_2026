package Back_Goblink_park.demo.repository;

import Back_Goblink_park.demo.entity.ActividadEvidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActividadEvidenciaRepository extends JpaRepository<ActividadEvidencia, Long> {
    List<ActividadEvidencia> findByActividadId(Long actividadId);
    void deleteByActividadId(Long actividadId);
}