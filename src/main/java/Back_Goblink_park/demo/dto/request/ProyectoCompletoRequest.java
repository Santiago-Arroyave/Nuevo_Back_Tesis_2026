package Back_Goblink_park.demo.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoCompletoRequest {

    // =====================================================
    // DATOS GENERALES DEL PROYECTO
    // =====================================================

    private ProyectoRequest proyecto;

    // =====================================================
    // REPORTES ASOCIADOS
    // =====================================================

    private List<Long> reporteIds;

    // =====================================================
    // MIEMBROS DEL PROYECTO
    // =====================================================

    private List<ProyectoMiembroRequest> miembros;

    // =====================================================
    // OBJETIVOS
    // =====================================================

    private List<ProyectoObjetivoRequest> objetivos;

    // =====================================================
    // METAS
    // =====================================================

    private List<ProyectoMetaRequest> metas;

    // =====================================================
    // PRESUPUESTO
    // =====================================================

    private List<ProyectoPresupuestoRequest> presupuesto;

    // =====================================================
    // CRONOGRAMA DE ACTIVIDADES
    // =====================================================

    private List<CronogramaActividadProyectoRequest> cronogramaActividades;




}