package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.response.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProyectoPdfService {

    private static final Locale LOCALE_ES_CO = Locale.forLanguageTag("es-CO");
    private static final DateTimeFormatter FECHA_CORTA_FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy", LOCALE_ES_CO);
    private static final DateTimeFormatter FECHA_GENERACION_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Pattern META_CANTIDAD_PATTERN =
            Pattern.compile("(\\d+[.,]?\\d*)\\s+([a-zA-ZáéíóúÁÉÍÓÚñÑ]{3,})");

    private final TemplateEngine templateEngine;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(8))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    // =====================================================
    // GENERAR PDF EDITORIAL (3 PÁGINAS ESTRICTAS)
    // =====================================================

    public byte[] generarPdfProyecto(ProyectoDetalleResponse detalle) {
        if (detalle == null || detalle.getProyecto() == null) {
            log.warn("[ProyectoPdfService] No se puede generar PDF: detalle o proyecto es nulo");
            return null;
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ProyectoResponse p = detalle.getProyecto();
            Context context = construirContextoThymeleaf(detalle, p);

            TemplateEngine engine = obtenerTemplateEngine();
            String xhtml = engine.process("pdf/proyecto-ficha", context);

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(xhtml, obtenerBaseUri());
            builder.toStream(out);
            builder.run();

            return out.toByteArray();
        } catch (Exception e) {
            log.error("[ProyectoPdfService] Error al generar PDF para proyecto #{}: {}",
                    detalle.getProyecto().getId(), e.getMessage(), e);
            return null;
        }
    }

    // =====================================================
    // CONSTRUCCIÓN DE CONTEXTO PARA LA PLANTILLA
    // =====================================================

    private Context construirContextoThymeleaf(ProyectoDetalleResponse detalle, ProyectoResponse p) {
        Context ctx = new Context(LOCALE_ES_CO);

        // 1. Datos de encabezado y portada (Página 1)
        ctx.setVariable("logoDataUri", cargarLogoDataUri());
        ctx.setVariable("idProyecto", p.getId() != null ? p.getId() : 0L);
        ctx.setVariable("nombreProyecto", textoSeguro(p.getNombre(), "Proyecto de Conservación Ambiental"));
        ctx.setVariable("estadoProyecto", formatearEtiqueta(p.getEstadoProyectoNombre(), "En ejecución"));
        ctx.setVariable("prioridadProyecto", formatearEtiqueta(p.getPrioridadNombre(), "Media"));
        ctx.setVariable("categoriaProyecto", formatearEtiqueta(p.getCategoriaNombre(), "Conservación"));
        ctx.setVariable("ubicacionProyecto", p.getUbicacion() != null && !p.getUbicacion().isBlank() ? p.getUbicacion().trim() : null);
        ctx.setVariable("descripcionProyecto", textoSeguro(
                p.getDescripcion(),
                "Proyecto de conservación, restauración ecológica y monitoreo participativo desarrollado en el Parque Natural Regional Páramo del Duende."
        ));

        // 2. Actividades y cálculo de KPI de avance
        List<CronogramaActividadProyectoResponse> rawActividades =
                detalle.getCronogramaActividades() != null ? detalle.getCronogramaActividades() : Collections.emptyList();

        int totalAct = rawActividades.size();
        long completadasCount = rawActividades.stream()
                .filter(this::esActividadCompletada)
                .count();

        int porcentajeAvance;
        if (p.getPorcentajeAvance() != null) {
            porcentajeAvance = (int) Math.round(p.getPorcentajeAvance());
        } else if (totalAct > 0) {
            porcentajeAvance = (int) Math.round((completadasCount * 100.0) / totalAct);
        } else {
            porcentajeAvance = 0;
        }

        ctx.setVariable("avanceTexto", porcentajeAvance + "%");
        ctx.setVariable("resumenActividadesTexto",
                completadasCount + " de " + totalAct + " actividades completadas");

        // 3. Presupuesto y cálculo de total
        List<PresupuestoPdfItem> presupuestoItems = new ArrayList<>();
        BigDecimal sumaPresupuesto = BigDecimal.ZERO;
        if (detalle.getPresupuesto() != null) {
            for (ProyectoPresupuestoResponse item : detalle.getPresupuesto()) {
                if (item == null) continue;
                BigDecimal monto = item.getMonto() != null ? item.getMonto() : BigDecimal.ZERO;
                sumaPresupuesto = sumaPresupuesto.add(monto);
                presupuestoItems.add(PresupuestoPdfItem.builder()
                        .rubro(textoSeguro(item.getRubro(), "Rubro operativo"))
                        .observaciones(textoSeguro(item.getObservaciones(), "Asignación presupuestal del proyecto"))
                        .montoFormateado(formatearMonedaCop(monto))
                        .build());
            }
        }

        BigDecimal presupuestoMostrar = (p.getPresupuestoEstimado() != null && p.getPresupuestoEstimado().compareTo(BigDecimal.ZERO) > 0)
                ? p.getPresupuestoEstimado()
                : sumaPresupuesto;

        BigDecimal totalTablaPresupuesto = sumaPresupuesto.compareTo(BigDecimal.ZERO) > 0
                ? sumaPresupuesto
                : presupuestoMostrar;

        ctx.setVariable("presupuestoEstimadoTexto", formatearMonedaCop(presupuestoMostrar));
        ctx.setVariable("presupuestoItems", presupuestoItems);
        ctx.setVariable("totalPresupuestoTexto", formatearMonedaCop(totalTablaPresupuesto));

        // 4. Periodo de ejecución
        String fechaIniStr = formatearFechaCorta(p.getFechaInicio());
        String fechaFinStr = formatearFechaCorta(p.getFechaFin());
        String periodoTexto;
        if (!"Sin definir".equals(fechaIniStr) && !"Sin definir".equals(fechaFinStr)) {
            periodoTexto = fechaIniStr + " al " + fechaFinStr;
        } else if (!"Sin definir".equals(fechaIniStr)) {
            periodoTexto = "Desde " + fechaIniStr;
        } else {
            periodoTexto = "Por definir";
        }
        ctx.setVariable("periodoTexto", periodoTexto);

        // 5. Sección 01: Objetivos de conservación
        List<ObjetivoPdfItem> objetivos = new ArrayList<>();
        if (detalle.getObjetivos() != null) {
            int idx = 1;
            for (ProyectoObjetivoResponse obj : detalle.getObjetivos()) {
                if (obj == null || obj.getDescripcion() == null || obj.getDescripcion().isBlank()) continue;
                objetivos.add(ObjetivoPdfItem.builder()
                        .numero(String.format("%02d", idx++))
                        .descripcion(obj.getDescripcion().trim())
                        .build());
            }
        }
        ctx.setVariable("objetivos", objetivos);

        // 6. Sección 02: Metas del proyecto (agrupadas en filas de máximo 3 tarjetas)
        List<MetaPdfItem> metasPlanas = new ArrayList<>();
        if (detalle.getMetas() != null) {
            int idx = 1;
            for (ProyectoMetaResponse meta : detalle.getMetas()) {
                if (meta == null || meta.getDescripcion() == null || meta.getDescripcion().isBlank()) continue;
                String descRaw = meta.getDescripcion().trim();
                metasPlanas.add(MetaPdfItem.builder()
                        .indicador(extraerIndicadorMeta(descRaw, idx))
                        .descripcion(extraerDescripcionMeta(descRaw))
                        .build());
                idx++;
            }
        }
        ctx.setVariable("metasFilas", particionarLista(metasPlanas, 3));

        // 7. Sección 04: Miembros del proyecto
        List<MiembroPdfItem> miembros = new ArrayList<>();
        if (detalle.getMiembros() != null) {
            for (ProyectoMiembroResponse m : detalle.getMiembros()) {
                if (m == null) continue;
                miembros.add(MiembroPdfItem.builder()
                        .nombre(textoSeguro(m.getUsuarioNombre(), "Integrante asignado"))
                        .correo(textoSeguro(m.getUsuarioCorreo(), "Sin correo registrado"))
                        .rol(formatearEtiqueta(m.getRolEnProyecto(), "Miembro"))
                        .build());
            }
        }
        ctx.setVariable("miembros", miembros);

        // 8. Sección 05 y Página 3: Cronograma de actividades + Evidencias fotográficas
        List<ActividadPdfItem> actividades = new ArrayList<>();
        int actIdx = 1;
        for (CronogramaActividadProyectoResponse act : rawActividades) {
            if (act == null) continue;
            boolean completada = esActividadCompletada(act);
            String numStr = String.format("%02d", act.getNumero() != null ? act.getNumero() : actIdx);
            actIdx++;

            String responsable = act.getUsuarioResponsableNombre() != null && !act.getUsuarioResponsableNombre().isBlank()
                    ? act.getUsuarioResponsableNombre().trim()
                    : (act.getProyectoMiembroRol() != null && !act.getProyectoMiembroRol().isBlank()
                    ? act.getProyectoMiembroRol().trim()
                    : "Equipo técnico");

            String observaciones = act.getObservaciones() != null && !act.getObservaciones().isBlank()
                    ? act.getObservaciones().trim()
                    : (act.getDescripcion() != null && !act.getDescripcion().isBlank()
                    ? act.getDescripcion().trim()
                    : "Actividad registrada dentro del plan operativo del proyecto.");

            String fotoDataUri = resolverEvidenciaDataUri(act);

            actividades.add(ActividadPdfItem.builder()
                    .numero(numStr)
                    .nombre(textoSeguro(act.getNombre(), "Actividad de campo"))
                    .fechaInicio(formatearFechaCorta(act.getFechaInicio()))
                    .fechaFin(formatearFechaCorta(act.getFechaFin()))
                    .responsable(responsable)
                    .completada(completada)
                    .estadoEtiqueta(completada ? "Completada" : formatearEtiqueta(act.getEstado(), "Pendiente"))
                    .observaciones(observaciones)
                    .fotoDataUri(fotoDataUri)
                    .build());
        }

        ctx.setVariable("actividades", actividades);
        ctx.setVariable("evidenciasFilas", particionarLista(actividades, 2));
        ctx.setVariable("fechaGeneracion", LocalDateTime.now().format(FECHA_GENERACION_FMT));

        return ctx;
    }

    // =====================================================
    // CARGA Y OPTIMIZACIÓN DE IMÁGENES A BASE64 DATA URI
    // =====================================================

    private String cargarLogoDataUri() {
        try {
            ClassPathResource resource = new ClassPathResource("static/logo.png");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    byte[] bytes = is.readAllBytes();
                    return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
                }
            }
        } catch (Exception e) {
            log.debug("[ProyectoPdfService] No se pudo cargar static/logo.png: {}", e.getMessage());
        }
        return null;
    }

    private String resolverEvidenciaDataUri(CronogramaActividadProyectoResponse act) {
        // 1. Intentar desde URL principal o lista de URLs/evidencias
        String candidateUrl = null;
        if (act.getUrlEvidencia() != null && !act.getUrlEvidencia().isBlank()) {
            candidateUrl = act.getUrlEvidencia().trim();
        } else if (act.getUrlsEvidencias() != null && !act.getUrlsEvidencias().isEmpty()) {
            for (String u : act.getUrlsEvidencias()) {
                if (u != null && !u.isBlank()) {
                    candidateUrl = u.trim();
                    break;
                }
            }
        } else if (act.getEvidencias() != null && !act.getEvidencias().isEmpty()) {
            for (ActividadEvidenciaResponse ev : act.getEvidencias()) {
                if (ev != null && ev.getUrlArchivo() != null && !ev.getUrlArchivo().isBlank()) {
                    candidateUrl = ev.getUrlArchivo().trim();
                    break;
                }
            }
        }

        if (candidateUrl != null) {
            if (candidateUrl.startsWith("data:image")) {
                return candidateUrl;
            }
            if (candidateUrl.startsWith("http://") || candidateUrl.startsWith("https://")) {
                String descargada = descargarYOptimizarImagen(candidateUrl);
                if (descargada != null) {
                    return descargada;
                }
            }
        }

        // 2. Fallback: Base64 en la actividad o en sus evidencias
        String rawBase64 = null;
        if (act.getImagenBase64() != null && !act.getImagenBase64().isBlank()) {
            rawBase64 = act.getImagenBase64().trim();
        } else if (act.getEvidencias() != null) {
            for (ActividadEvidenciaResponse ev : act.getEvidencias()) {
                if (ev != null && ev.getArchivoBase64() != null && !ev.getArchivoBase64().isBlank()) {
                    rawBase64 = ev.getArchivoBase64().trim();
                    break;
                }
            }
        }

        if (rawBase64 != null) {
            return normalizarBase64DataUri(rawBase64);
        }

        return null;
    }

    private String descargarYOptimizarImagen(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url.replace(" ", "%20")))
                    .timeout(Duration.ofSeconds(8))
                    .header("User-Agent", "GoblinPark-PDF-Renderer/1.0")
                    .GET()
                    .build();

            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() >= 200 && response.statusCode() < 300 && response.body() != null && response.body().length > 0) {
                return optimizarBytesImagenADataUri(response.body());
            }
        } catch (Exception e) {
            log.warn("[ProyectoPdfService] No se pudo descargar evidencia desde URL {}: {}", url, e.getMessage());
        }
        return null;
    }

    private String normalizarBase64DataUri(String base64Input) {
        try {
            String clean = base64Input;
            if (clean.contains(",")) {
                clean = clean.substring(clean.indexOf(',') + 1);
            }
            byte[] decoded = Base64.getDecoder().decode(clean.replaceAll("\\s+", ""));
            String optimizada = optimizarBytesImagenADataUri(decoded);
            if (optimizada != null) {
                return optimizada;
            }
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(decoded);
        } catch (Exception e) {
            log.warn("[ProyectoPdfService] Error al procesar imagen Base64 para PDF: {}", e.getMessage());
            return null;
        }
    }

    private String optimizarBytesImagenADataUri(byte[] imageBytes) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
            BufferedImage original = ImageIO.read(bais);
            if (original == null) {
                return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
            }

            int maxWidth = 640;
            int maxHeight = 420;
            int width = original.getWidth();
            int height = original.getHeight();

            if (width > maxWidth || height > maxHeight) {
                double ratio = Math.min((double) maxWidth / width, (double) maxHeight / height);
                width = Math.max(1, (int) Math.round(width * ratio));
                height = Math.max(1, (int) Math.round(height * ratio));
            }

            BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resized.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);
            g2d.drawImage(original, 0, 0, width, height, null);
            g2d.dispose();

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(resized, "jpg", baos);
                return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
            }
        } catch (Exception e) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
        }
    }

    // =====================================================
    // UTILIDADES DE FORMATO Y EXTRACCIÓN
    // =====================================================

    private boolean esActividadCompletada(CronogramaActividadProyectoResponse act) {
        if (act == null) return false;
        if (act.getEstado() != null) {
            String est = act.getEstado().trim().toUpperCase();
            if (est.contains("COMPLETAD") || est.contains("FINALIZAD") || est.equals("CUMPLIDA")) {
                return true;
            }
        }
        return act.getPorcentajeAvance() != null
                && act.getPorcentajeAvance().compareTo(BigDecimal.valueOf(100)) >= 0;
    }

    private String formatearMonedaCop(BigDecimal monto) {
        BigDecimal valor = monto != null ? monto : BigDecimal.ZERO;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(LOCALE_ES_CO);
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0", symbols);
        return "$ " + df.format(valor);
    }

    private String formatearFechaCorta(LocalDate fecha) {
        if (fecha == null) {
            return "Sin definir";
        }
        return fecha.format(FECHA_CORTA_FMT).replace(".", "");
    }

    private String formatearEtiqueta(String valor, String porDefecto) {
        if (valor == null || valor.isBlank()) {
            return porDefecto;
        }
        String limpio = valor.trim().replace('_', ' ').toLowerCase(LOCALE_ES_CO);
        if (limpio.equals("planificacion")) {
            return "Planificación";
        }
        if (limpio.equals("en ejecucion")) {
            return "En ejecución";
        }
        return Character.toUpperCase(limpio.charAt(0)) + limpio.substring(1);
    }

    private String extraerIndicadorMeta(String descripcion, int index) {
        if (descripcion == null || descripcion.isBlank()) {
            return String.format("META %02d", index);
        }
        if (descripcion.contains(":")) {
            String prefijo = descripcion.substring(0, descripcion.indexOf(':')).trim();
            if (prefijo.length() >= 2 && prefijo.length() <= 24) {
                return prefijo.toUpperCase(LOCALE_ES_CO);
            }
        }
        Matcher matcher = META_CANTIDAD_PATTERN.matcher(descripcion);
        if (matcher.find()) {
            String numero = matcher.group(1);
            String palabra = matcher.group(2).toUpperCase(LOCALE_ES_CO);
            if (numero.length() == 1) {
                numero = "0" + numero;
            }
            return numero + " " + palabra;
        }
        return String.format("META %02d", index);
    }

    private String extraerDescripcionMeta(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            return "";
        }
        if (descripcion.contains(":")) {
            String prefijo = descripcion.substring(0, descripcion.indexOf(':')).trim();
            String resto = descripcion.substring(descripcion.indexOf(':') + 1).trim();
            if (prefijo.length() <= 24 && !resto.isEmpty()) {
                return resto;
            }
        }
        return descripcion;
    }

    private String textoSeguro(String valor, String fallback) {
        return (valor != null && !valor.isBlank()) ? valor.trim() : fallback;
    }

    private <T> List<List<T>> particionarLista(List<T> lista, int tamano) {
        if (lista == null || lista.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<T>> particiones = new ArrayList<>();
        for (int i = 0; i < lista.size(); i += tamano) {
            particiones.add(lista.subList(i, Math.min(i + tamano, lista.size())));
        }
        return particiones;
    }

    private TemplateEngine obtenerTemplateEngine() {
        if (this.templateEngine != null) {
            return this.templateEngine;
        }
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        SpringTemplateEngine fallbackEngine = new SpringTemplateEngine();
        fallbackEngine.setTemplateResolver(resolver);
        return fallbackEngine;
    }

    private String obtenerBaseUri() {
        try {
            ClassPathResource resource = new ClassPathResource("static/");
            if (resource.exists()) {
                return resource.getURL().toExternalForm();
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    // =====================================================
    // MODELOS DE VISTA INTERNOS PARA THYMELEAF
    // =====================================================

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ObjetivoPdfItem {
        private final String numero;
        private final String descripcion;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MetaPdfItem {
        private final String indicador;
        private final String descripcion;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PresupuestoPdfItem {
        private final String rubro;
        private final String observaciones;
        private final String montoFormateado;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MiembroPdfItem {
        private final String nombre;
        private final String correo;
        private final String rol;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ActividadPdfItem {
        private final String numero;
        private final String nombre;
        private final String fechaInicio;
        private final String fechaFin;
        private final String responsable;
        private final boolean completada;
        private final String estadoEtiqueta;
        private final String observaciones;
        private final String fotoDataUri;
    }
}