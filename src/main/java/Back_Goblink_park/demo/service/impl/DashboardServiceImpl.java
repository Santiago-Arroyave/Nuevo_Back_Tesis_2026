package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.response.dashboard.*;
import Back_Goblink_park.demo.entity.CronogramaActividadProyecto;
import Back_Goblink_park.demo.repository.CronogramaActividadProyectoRepository;
import Back_Goblink_park.demo.repository.ProyectoMiembroRepository;
import Back_Goblink_park.demo.repository.ProyectoRepository;
import Back_Goblink_park.demo.repository.ReporteRepository;
import Back_Goblink_park.demo.repository.UsuarioRepository;
import Back_Goblink_park.demo.service.interfaces.DashboardService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    // =====================================================
    // REPOSITORIOS INYECTADOS
    // =====================================================
    private final ReporteRepository reporteRepository;
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProyectoMiembroRepository proyectoMiembroRepository;
    private final CronogramaActividadProyectoRepository cronogramaRepository;

    // =========================================
    // RESUMEN DASHBOARD
    // =========================================
    @Override
    public DashboardResumenResponse obtenerResumen() {

        List<CronogramaActividadProyecto> actividadesVencidas = cronogramaRepository.findAll()
                .stream()
                .filter(a -> a.getFechaFin() != null &&
                        a.getFechaFin().isBefore(LocalDate.now()) &&
                        !"completado".equalsIgnoreCase(a.getEstado()))
                .toList();

        return DashboardResumenResponse.builder()
                .totalReportes(reporteRepository.countByEliminadoFalse())
                .reportesPendientes(reporteRepository.countByEstadoReporteIdAndEliminadoFalse(1L))
                .reportesEnRevision(reporteRepository.countByEstadoReporteIdAndEliminadoFalse(5L))
                .reportesResueltos(reporteRepository.countByEstadoReporteIdAndEliminadoFalse(3L))
                .reportesConvertidosProyecto(reporteRepository.countByEstadoReporteIdAndEliminadoFalse(7L))
                .reportesCriticos(reporteRepository.countByPrioridadIdAndEliminadoFalse(4L))
                .actividadesVencidas((long) actividadesVencidas.size())

                // ✅ CORREGIDO: Usar estadoProyectoId específico
                .proyectosActivos(proyectoRepository.countByEstadoProyectoIdAndEliminadoFalse(2L)) // 2 = En ejecución
                .proyectosFinalizados(proyectoRepository.countByEstadoProyectoIdAndEliminadoFalse(4L)) // 4 = Finalizado

                .miembrosActivos(usuarioRepository.countByEstadoTrue())
                .build();
    }

    // =========================================
    // REPORTES POR CATEGORÍA
    // =========================================
    @Override
    public List<DashboardGraficaResponse> obtenerReportesPorCategoria() {
        return reporteRepository.countReportesPorCategoria()
                .stream()
                .map(resultado -> DashboardGraficaResponse.builder()
                        .nombre((String) resultado[0])      // nombre de la categoría
                        .cantidad((Long) resultado[1])       // cantidad de reportes
                        .color((String) resultado[2])        // ← color de la categoría
                        .build())
                .collect(Collectors.toList());
    }

    // =========================================
    // REPORTES POR PRIORIDAD
    // =========================================
    @Override
    public List<DashboardGraficaResponse> obtenerReportesPorPrioridad() {
        return reporteRepository.countReportesPorPrioridad()
                .stream()
                .map(resultado -> DashboardGraficaResponse.builder()
                        .nombre((String) resultado[0])      // nombre de la prioridad
                        .cantidad((Long) resultado[1])       // cantidad de reportes
                        .color((String) resultado[2])        // ← color de la prioridad
                        .build())
                .collect(Collectors.toList());
    }

    // =========================================
    // ACTIVIDADES VENCIDAS - ✅ CORREGIDO
    // =========================================
    @Override
    public List<ActividadVencidaDTO> obtenerActividadesVencidas() {
        LocalDate hoy = LocalDate.now();

        // Busca actividades con fecha fin < hoy y estado != completado
        List<CronogramaActividadProyecto> vencidas = cronogramaRepository.findAll()
                .stream()
                .filter(a -> a.getFechaFin() != null &&
                        a.getFechaFin().isBefore(hoy) &&
                        !"COMPLETADO".equalsIgnoreCase(a.getEstado()))
                .limit(5)
                .toList();

        return vencidas.stream().map(a -> {
            // ✅ EXTRAER NOMBRE DEL RESPONSABLE DESDE ProyectoMiembro
            String nombreResponsable = "Sin asignar";

            if (a.getProyectoMiembro() != null &&
                    a.getProyectoMiembro().getUsuario() != null) {
                nombreResponsable = a.getProyectoMiembro().getUsuario().getNombres();
            }

            return ActividadVencidaDTO.builder()
                    .id(a.getId())
                    .proyectoId(a.getProyecto().getId())
                    .nombreActividad(a.getNombre())
                    .proyectoNombre(a.getProyecto() != null ? a.getProyecto().getNombre() : "Sin Proyecto")
                    .fechaLimite(a.getFechaFin())
                    .diasRetraso((int) java.time.temporal.ChronoUnit.DAYS.between(a.getFechaFin(), hoy))
                    .responsableNombre(nombreResponsable)
                    .build();
        }).toList();
    }

    // =========================================
    // ACTIVIDAD RECIENTE (FEED)
    // =========================================
    @Override
    public List<ActividadRecienteDTO> obtenerActividadReciente() {
        List<ActividadRecienteDTO> actividades = new ArrayList<>();

        // Últimos 5 reportes
        reporteRepository.findTop5ByOrderByFechaReporteDesc().forEach(r ->
                actividades.add(ActividadRecienteDTO.builder()
                        .tipo("REPORTE")
                        .titulo("Nuevo reporte: " + (r.getTitulo() != null ? r.getTitulo() : "Sin título"))
                        .descripcion((r.getUsuario() != null ? r.getUsuario().getNombres() : "Usuario") + " registró un reporte.")
                        .usuarioNombre(r.getUsuario() != null ? r.getUsuario().getNombres() : "Anónimo")
                        .fecha(r.getFechaReporte())
                        .build())
        );

        // Últimos 5 proyectos
        proyectoRepository.findTop5ByOrderByCreatedAtDesc().forEach(p ->
                actividades.add(ActividadRecienteDTO.builder()
                        .tipo("PROYECTO")
                        .titulo("Proyecto creado: " + p.getNombre())
                        .descripcion("Iniciado por el equipo administrativo.")
                        .usuarioNombre("Admin")
                        .fecha(p.getCreatedAt())
                        .build())
        );

        // Ordenar por fecha descendente y limitar a 10
        return actividades.stream()
                .sorted(Comparator.comparing(ActividadRecienteDTO::getFecha).reversed())
                .limit(10)
                .toList();
    }

    // =========================================
    // EVOLUCIÓN DE REPORTES (GRÁFICA LÍNEA)
    // =========================================
    @Override
    public List<EvolucionReporteDTO> obtenerEvolucionReportes(Integer dias) {
        LocalDate fechaInicio = LocalDate.now().minusDays(dias);

        return reporteRepository.findAll().stream()
                .filter(r -> r.getFechaReporte() != null &&
                        !r.getFechaReporte().toLocalDate().isBefore(fechaInicio))
                .collect(Collectors.groupingBy(
                        r -> r.getFechaReporte().toLocalDate(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(e -> EvolucionReporteDTO.builder()
                        .fecha(e.getKey())
                        .cantidad(e.getValue())
                        .build())
                .sorted(Comparator.comparing(EvolucionReporteDTO::getFecha))
                .toList();
    }
}