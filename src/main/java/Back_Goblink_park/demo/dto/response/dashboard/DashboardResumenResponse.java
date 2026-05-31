package Back_Goblink_park.demo.dto.response.dashboard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResumenResponse {

    private Long totalReportes;

    private Long reportesPendientes;

    private Long reportesEnRevision;  // ✅ CAMBIO: de "reportesProceso" a "reportesEnRevision"

    private Long reportesResueltos;

    private Long reportesConvertidosProyecto;  // ✅ AGREGAR

    private Long reportesCriticos;  // ✅ AGREGAR

    private Long actividadesVencidas;

    private Long proyectosActivos;

    private Long proyectosFinalizados;  // ✅ AGREGAR

    private Long miembrosActivos;  // ✅ CAMBIO: de "totalMiembros" a "miembrosActivos"
}