package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.CronogramaActividadProyectoResponse;
import Back_Goblink_park.demo.entity.CronogramaActividadProyecto;

public class CronogramaActividadProyectoMapper {

        public static CronogramaActividadProyectoResponse toResponse(CronogramaActividadProyecto actividad) {
                if (actividad == null) return null;

                return CronogramaActividadProyectoResponse.builder()
                        .id(actividad.getId())
                        .proyectoId(actividad.getProyecto() != null ? actividad.getProyecto().getId() : null)
                        .proyectoNombre(actividad.getProyecto() != null ? actividad.getProyecto().getNombre() : null)
                        .proyectoMiembroId(actividad.getProyectoMiembro() != null ? actividad.getProyectoMiembro().getId() : null)
                        .proyectoMiembroRol(actividad.getProyectoMiembro() != null ? actividad.getProyectoMiembro().getRolEnProyecto() : null)
                        .usuarioResponsableId(actividad.getProyectoMiembro() != null ? actividad.getProyectoMiembro().getUsuario().getId() : null)
                        .usuarioResponsableNombre(actividad.getProyectoMiembro() != null ? actividad.getProyectoMiembro().getUsuario().getNombres() : null)
                        .usuarioResponsableEmail(actividad.getProyectoMiembro() != null ? actividad.getProyectoMiembro().getUsuario().getCorreo() : null)
                        .nombre(actividad.getNombre())
                        .descripcion(actividad.getDescripcion())
                        .estado(actividad.getEstado())
                        .prioridad(actividad.getPrioridad())
                        .porcentajeAvance(actividad.getPorcentajeAvance())
                        .fechaInicio(actividad.getFechaInicio())
                        .fechaFin(actividad.getFechaFin())
                        .urlEvidencia(actividad.getUrlEvidencia())
                        .observaciones(actividad.getObservaciones())

                        // ✅ MAPEO DE LA LISTA DE EVIDENCIAS usando la clase separada
                        .evidencias(
                                actividad.getEvidencias() != null
                                        ? actividad.getEvidencias().stream()
                                        .map(ActividadEvidenciaMapper::toResponse)
                                        .toList()
                                        : null
                        )
                        .build();
        }
}