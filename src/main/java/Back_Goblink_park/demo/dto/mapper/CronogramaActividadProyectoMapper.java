package Back_Goblink_park.demo.dto.mapper;

import Back_Goblink_park.demo.dto.response.ActividadEvidenciaResponse;
import Back_Goblink_park.demo.dto.response.CronogramaActividadProyectoResponse;
import Back_Goblink_park.demo.entity.CronogramaActividadProyecto;

import java.util.Collections;
import java.util.List;

public class CronogramaActividadProyectoMapper {

        public static CronogramaActividadProyectoResponse toResponse(CronogramaActividadProyecto actividad) {
                if (actividad == null) return null;

                List<ActividadEvidenciaResponse> evidenciasDto = actividad.getEvidencias() != null
                        ? actividad.getEvidencias().stream()
                        .map(ActividadEvidenciaMapper::toResponse)
                        .toList()
                        : Collections.emptyList();

                List<String> urls = evidenciasDto.stream()
                        .map(ActividadEvidenciaResponse::getUrlArchivo)
                        .filter(url -> url != null && !url.isBlank())
                        .toList();

                return CronogramaActividadProyectoResponse.builder()
                        .id(actividad.getId())
                        .proyectoId(actividad.getProyecto() != null ? actividad.getProyecto().getId() : null)
                        .proyectoNombre(actividad.getProyecto() != null ? actividad.getProyecto().getNombre() : null)
                        .proyectoMiembroId(actividad.getProyectoMiembro() != null ? actividad.getProyectoMiembro().getId() : null)
                        .proyectoMiembroRol(actividad.getProyectoMiembro() != null ? actividad.getProyectoMiembro().getRolEnProyecto() : null)
                        .usuarioResponsableId(actividad.getProyectoMiembro() != null && actividad.getProyectoMiembro().getUsuario() != null ? actividad.getProyectoMiembro().getUsuario().getId() : null)
                        .usuarioResponsableNombre(actividad.getProyectoMiembro() != null && actividad.getProyectoMiembro().getUsuario() != null ? actividad.getProyectoMiembro().getUsuario().getNombres() : null)
                        .usuarioResponsableEmail(actividad.getProyectoMiembro() != null && actividad.getProyectoMiembro().getUsuario() != null ? actividad.getProyectoMiembro().getUsuario().getCorreo() : null)
                        .nombre(actividad.getNombre())
                        .descripcion(actividad.getDescripcion())
                        .estado(actividad.getEstado())
                        .prioridad(actividad.getPrioridad())
                        .porcentajeAvance(actividad.getPorcentajeAvance())
                        .fechaInicio(actividad.getFechaInicio())
                        .fechaFin(actividad.getFechaFin())
                        .urlEvidencia(actividad.getUrlEvidencia())
                        .observaciones(actividad.getObservaciones())
                        .evidencias(evidenciasDto)
                        .urlsEvidencias(urls)
                        .build();
        }
}