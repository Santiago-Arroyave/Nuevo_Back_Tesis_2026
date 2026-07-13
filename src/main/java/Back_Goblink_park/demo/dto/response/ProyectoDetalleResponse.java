package Back_Goblink_park.demo.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoDetalleResponse {

    // =====================================================
    // DATOS GENERALES DEL PROYECTO
    // =====================================================

    private ProyectoResponse proyecto;

    // =====================================================
    // RELACIONES DEL PROYECTO
    // =====================================================

    private List<ProyectoReporteResponse> reportesAsociados;

    private List<ProyectoMiembroResponse> miembros;

    private List<ProyectoObjetivoResponse> objetivos;

    private List<ProyectoMetaResponse> metas;

    private List<ProyectoPresupuestoResponse> presupuesto;

    private List<CronogramaActividadProyectoResponse> cronogramaActividades;

    private List<SeguimientoProyectoResponse> seguimientos;
}