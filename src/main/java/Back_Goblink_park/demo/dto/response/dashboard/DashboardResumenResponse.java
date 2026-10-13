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

    private Long reportesEnRevision;

    private Long reportesResueltos;

    private Long reportesConvertidosProyecto;

    private Long convertidosEnProyecto;

    private Long reportesCriticos;

    private Long actividadesVencidas;

    private Long proyectosActivos;

    private Long proyectosEnCurso;

    private Long proyectosFinalizados;

    private Long miembrosActivos;
}