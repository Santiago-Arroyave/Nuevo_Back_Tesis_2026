package Back_Goblink_park.demo.service.impl;

import Back_Goblink_park.demo.dto.response.*;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class ProyectoPdfService {

    public byte[] generarPdfProyecto(ProyectoDetalleResponse proyecto) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 40, 40, 20, 40);
            PdfWriter writer = PdfWriter.getInstance(document, out);

            // Fondo decorativo
            writer.setPageEvent(new FondoConImagenEvent());

            document.open();

            // =============================================
            // ESTILOS
            // =============================================
            Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(20, 80, 20));
            Font subtituloFont = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(20, 80, 20));
            Font textoFont = new Font(Font.HELVETICA, 9, Font.NORMAL);
            Font textoBoldFont = new Font(Font.HELVETICA, 9, Font.BOLD);
            Font smallFont = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.GRAY);
            Color verdeOscuro = new Color(20, 80, 20);
            Color verdeClaro = new Color(240, 248, 240);

            // =============================================
            // HEADER CON LOGO Y TÍTULO
            // =============================================
            // Tabla para alinear logo y título horizontalmente
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 5});
            headerTable.setSpacingAfter(10);
            // Logo
            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            try {
                ClassPathResource logoResource = new ClassPathResource("static/logo.png");
                if (logoResource.exists()) {
                    Image logo = Image.getInstance(logoResource.getURL().toString());
                    logo.scaleToFit(60, 60);
                    logoCell.addElement(logo);
                }
            } catch (Exception e) {
                // Si no hay logo, dejar celda vacía
            }

            headerTable.addCell(logoCell);

            // Título
            PdfPCell titleCell = new PdfPCell();
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            Paragraph titulo = new Paragraph(proyecto.getProyecto().getNombre(), tituloFont);
            titleCell.addElement(titulo);

            headerTable.addCell(titleCell);

            // Agregar al documento
            document.add(headerTable);
            document.add(new Paragraph(" "));


            // =============================================
            // INFORMACIÓN GENERAL
            // =============================================
            document.add(new Paragraph("INFORMACION GENERAL", subtituloFont));
            document.add(new Paragraph(" "));

            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(100);
            tablaInfo.setWidths(new float[]{2, 3});

            agregarCeldaInfo(tablaInfo, "Estado:", proyecto.getProyecto().getEstadoProyectoNombre(), textoFont, verdeClaro);
            agregarCeldaInfo(tablaInfo, "Prioridad:", proyecto.getProyecto().getPrioridadNombre(), textoFont, verdeClaro);
            agregarCeldaInfo(tablaInfo, "Categoria:",
                    proyecto.getProyecto().getCategoriaNombre() != null ? proyecto.getProyecto().getCategoriaNombre() : "Sin categoria",
                    textoFont, verdeClaro);
            agregarCeldaInfo(tablaInfo, "Fecha Inicio:", formatearFecha(proyecto.getProyecto().getFechaInicio()), textoFont, verdeClaro);
            agregarCeldaInfo(tablaInfo, "Fecha Fin:", formatearFecha(proyecto.getProyecto().getFechaFin()), textoFont, verdeClaro);
            agregarCeldaInfo(tablaInfo, "Presupuesto:", "$ " + proyecto.getProyecto().getPresupuestoEstimado(), textoFont, verdeClaro);
            agregarCeldaInfo(tablaInfo, "Avance:", proyecto.getProyecto().getPorcentajeAvance() + "%", textoBoldFont, verdeClaro);

            document.add(tablaInfo);
            document.add(new Paragraph(" "));

            // Descripción
            if (proyecto.getProyecto().getDescripcion() != null && !proyecto.getProyecto().getDescripcion().isEmpty()) {
                document.add(new Paragraph("Descripcion:", textoBoldFont));
                document.add(new Paragraph(proyecto.getProyecto().getDescripcion(), textoFont));
                document.add(new Paragraph(" "));
            }

            // =============================================
            // MIEMBROS DEL PROYECTO
            if (proyecto.getMiembros() != null && !proyecto.getMiembros().isEmpty()) {
                agregarTituloConIcono(document, "MIEMBROS DEL PROYECTO", "icono-miembros.png", subtituloFont);
                document.add(new Paragraph(" "));

                PdfPTable tablaMiembros = new PdfPTable(3);
                tablaMiembros.setWidthPercentage(100);
                tablaMiembros.setWidths(new float[]{2, 3, 1});
                tablaMiembros.setSpacingBefore(5);

                agregarCeldaEncabezado(tablaMiembros, "Nombre", verdeOscuro);
                agregarCeldaEncabezado(tablaMiembros, "Email", verdeOscuro);
                agregarCeldaEncabezado(tablaMiembros, "Rol", verdeOscuro);

                for (ProyectoMiembroResponse miembro : proyecto.getMiembros()) {
                    agregarCeldaSimple(tablaMiembros, miembro.getUsuarioNombre(), textoFont);
                    agregarCeldaSimple(tablaMiembros,
                            miembro.getUsuarioCorreo() != null ? miembro.getUsuarioCorreo() : "-", textoFont);
                    agregarCeldaSimple(tablaMiembros, miembro.getRolEnProyecto(), textoFont);
                }

                document.add(tablaMiembros);
                document.add(new Paragraph(" "));
            }

            // =============================================
            // OBJETIVOS
            // =============================================
            if (proyecto.getObjetivos() != null && !proyecto.getObjetivos().isEmpty()) {
                agregarTituloConIcono(document, "OBJETIVOS", "icono-objetivo.png", subtituloFont);
                document.add(new Paragraph(" "));

                for (int i = 0; i < proyecto.getObjetivos().size(); i++) {
                    ProyectoObjetivoResponse objetivo = proyecto.getObjetivos().get(i);
                    document.add(new Paragraph((i + 1) + ". " + objetivo.getDescripcion(), textoFont));
                }
                document.add(new Paragraph(" "));
            }

            // =============================================
            // METAS
            // =============================================
            if (proyecto.getMetas() != null && !proyecto.getMetas().isEmpty()) {
                agregarTituloConIcono(document, "METAS", "icono-meta.png", subtituloFont);
                document.add(new Paragraph(" "));


                for (int i = 0; i < proyecto.getMetas().size(); i++) {
                    ProyectoMetaResponse meta = proyecto.getMetas().get(i);
                    document.add(new Paragraph((i + 1) + ". " + meta.getDescripcion(), textoFont));
                }
                document.add(new Paragraph(" "));
            }

            // =============================================
            // PRESUPUESTO
            // =============================================
            if (proyecto.getPresupuesto() != null && !proyecto.getPresupuesto().isEmpty()) {
                agregarTituloConIcono(document, "PRESUPUESTO", "icono-presupuesto.png", subtituloFont);

                PdfPTable tablaPresupuesto = new PdfPTable(3);
                tablaPresupuesto.setWidthPercentage(100);
                tablaPresupuesto.setWidths(new float[]{2, 3, 1});
                tablaPresupuesto.setSpacingBefore(5);

                agregarCeldaEncabezado(tablaPresupuesto, "Rubro", verdeOscuro);
                agregarCeldaEncabezado(tablaPresupuesto, "Observaciones", verdeOscuro);
                agregarCeldaEncabezado(tablaPresupuesto, "Monto", verdeOscuro);

                BigDecimal total = BigDecimal.ZERO;
                for (ProyectoPresupuestoResponse item : proyecto.getPresupuesto()) {
                    agregarCeldaSimple(tablaPresupuesto, item.getRubro(), textoFont);
                    agregarCeldaSimple(tablaPresupuesto,
                            item.getObservaciones() != null ? item.getObservaciones() : "-", textoFont);
                    agregarCeldaSimple(tablaPresupuesto, "$ " + item.getMonto().setScale(2, RoundingMode.HALF_UP), textoFont);
                    total = total.add(item.getMonto());
                }

                // Total
                PdfPCell cellTotal1 = new PdfPCell(new Phrase("TOTAL", textoBoldFont));
                cellTotal1.setBackgroundColor(new Color(200, 200, 200));
                cellTotal1.setBorder(Rectangle.BOX);
                cellTotal1.setPadding(5);
                tablaPresupuesto.addCell(cellTotal1);

                PdfPCell cellTotal2 = new PdfPCell(new Phrase("", textoFont));
                cellTotal2.setBackgroundColor(new Color(200, 200, 200));
                cellTotal2.setBorder(Rectangle.BOX);
                cellTotal2.setPadding(5);
                tablaPresupuesto.addCell(cellTotal2);

                PdfPCell cellTotal3 = new PdfPCell(new Phrase("$ " + total.setScale(2, RoundingMode.HALF_UP), textoBoldFont));
                cellTotal3.setBackgroundColor(new Color(200, 200, 200));
                cellTotal3.setBorder(Rectangle.BOX);
                cellTotal3.setPadding(5);
                tablaPresupuesto.addCell(cellTotal3);

                document.add(tablaPresupuesto);
                document.add(new Paragraph(" "));
            }

            // =============================================
            // CRONOGRAMA DE ACTIVIDADES
            // =============================================
            if (proyecto.getCronogramaActividades() != null &&
                    !proyecto.getCronogramaActividades().isEmpty()) {

                agregarTituloConIcono(document, "CRONOGRAMA DE ACTIVIDADES", "icono-cronograma.png", subtituloFont);
                document.add(new Paragraph(" "));

                for (CronogramaActividadProyectoResponse actividad : proyecto.getCronogramaActividades()) {

                    // Espacio antes de cada actividad
                    document.add(new Paragraph(" "));

                    // Título de la actividad
                    Paragraph tituloAct = new Paragraph("Actividad: " + actividad.getNombre(), textoBoldFont);
                    document.add(tituloAct);
                    document.add(new Paragraph(" "));

                    // Información básica
                    document.add(new Paragraph("   Inicio: " + formatearFecha(actividad.getFechaInicio()), textoFont));
                    document.add(new Paragraph("   Fin: " + formatearFecha(actividad.getFechaFin()), textoFont));

                    String responsable = actividad.getUsuarioResponsableNombre() != null
                            ? actividad.getUsuarioResponsableNombre()
                            : "Sin asignar";
                    document.add(new Paragraph("   Responsable: " + responsable, textoFont));

                    // Estado con color
                    Color estadoColor = "completada".equalsIgnoreCase(actividad.getEstado())
                            ? new Color(0, 150, 0)
                            : new Color(255, 165, 0);
                    Font estadoFont = new Font(Font.HELVETICA, 9, Font.BOLD, estadoColor);
                    document.add(new Paragraph("   Estado: " + actividad.getEstado(), estadoFont));

                    // Evidencia si está completada
                    if ("completada".equalsIgnoreCase(actividad.getEstado())) {

                        document.add(new Paragraph(" "));

                        // Descripción
                        if (actividad.getObservaciones() != null && !actividad.getObservaciones().isEmpty()) {
                            document.add(new Paragraph("   Descripcion:", textoBoldFont));
                            document.add(new Paragraph("   " + actividad.getObservaciones(), textoFont));
                        }

                        /// ✅ IMAGEN BASE64 - CON MÁS DEBUG
                        if (actividad.getImagenBase64() != null && !actividad.getImagenBase64().isEmpty()) {
                            try {
                                document.add(new Paragraph(" "));
                                document.add(new Paragraph("   Evidencia fotografica:", textoBoldFont));
                                document.add(new Paragraph(" "));

                                // Limpiar prefijo si existe
                                String base64Limpio = actividad.getImagenBase64();
                                if (base64Limpio.contains(",")) {
                                    base64Limpio = base64Limpio.split(",")[1];
                                }

                                System.out.println("========================================");
                                System.out.println("📸 PROCESANDO IMAGEN BASE64");
                                System.out.println("Tamaño Base64: " + base64Limpio.length() + " caracteres");
                                System.out.println("Primeros 20 chars: " + base64Limpio.substring(0, Math.min(20, base64Limpio.length())));

                                // Decodificar Base64
                                byte[] imageBytes = Base64.getDecoder().decode(base64Limpio);
                                System.out.println("Bytes decodificados: " + imageBytes.length + " bytes (" + (imageBytes.length / 1024 / 1024) + " MB)");

// Crear imagen
                                Image img = Image.getInstance(imageBytes);
                                System.out.println("Dimensiones originales: " + img.getWidth() + "x" + img.getHeight() + " px");

// ✅ ESCALADO FORZADO - Reducir a 200px max
                                float anchoMax = 200f;
                                float altoMax = 300f;  // Altura máxima para que no ocupe toda la página

// Escalar proporcionalmente
                                img.scaleToFit(anchoMax, altoMax);
                                System.out.println("Imagen escalada FORZADAMENTE a: " + img.getWidth() + "x" + img.getHeight() + " px");

// Centrar imagen
                                img.setAlignment(Element.ALIGN_CENTER);
                                img.setSpacingBefore(5);
                                img.setSpacingAfter(5);

// Agregar borde
                                img.setBorder(Rectangle.BOX);
                                img.setBorderWidth(0.5f);
                                img.setBorderColor(Color.LIGHT_GRAY);

                                System.out.println("Agregando imagen al documento...");
                                document.add(img);
                                System.out.println("✅ IMAGEN AGREGADA CORRECTAMENTE");

                            } catch (OutOfMemoryError e) {
                                System.err.println("❌ ERROR: Imagen demasiado grande (OutOfMemoryError)");
                                System.err.println("   Tamaño Base64: " + actividad.getImagenBase64().length());
                                e.printStackTrace();

                                document.add(new Paragraph(
                                        "   [La imagen es demasiado grande para mostrarse en el PDF]",
                                        new Font(Font.HELVETICA, 8, Font.ITALIC, Color.RED)
                                ));

                            } catch (Exception e) {
                                System.err.println("❌ Error al cargar imagen: " + e.getMessage());
                                System.err.println("   Tipo de error: " + e.getClass().getName());
                                e.printStackTrace();

                                document.add(new Paragraph(
                                        "   [Error al cargar la imagen: " + e.getMessage() + "]",
                                        new Font(Font.HELVETICA, 8, Font.ITALIC, Color.RED)
                                ));
                            }
                        }
                    }

                    // Línea separadora AL FINAL de cada actividad
                    document.add(new Paragraph(" "));
                    LineSeparator lineaSep = new LineSeparator();
                    lineaSep.setLineColor(verdeOscuro);
                    lineaSep.setLineWidth(1f);
                    document.add(lineaSep);
                }
            }
            // =============================================
            // SEGUIMIENTOS
            // =============================================
            if (proyecto.getSeguimientos() != null && !proyecto.getSeguimientos().isEmpty()) {
                document.add(new Paragraph("SEGUIMIENTOS DEL PROYECTO", subtituloFont));
                document.add(new Paragraph(" "));

                for (SeguimientoProyectoResponse seg : proyecto.getSeguimientos()) {
                    document.add(new Paragraph("Fecha: " + formatearFechaHora(seg.getFechaSeguimiento()), textoBoldFont));
                    document.add(new Paragraph("Avance: " + seg.getPorcentajeAvance() + "%", textoFont));
                    document.add(new Paragraph("Descripcion: " + seg.getDescripcionAvance(), textoFont));
                    if (seg.getObservaciones() != null && !seg.getObservaciones().isEmpty()) {
                        document.add(new Paragraph("Observaciones: " + seg.getObservaciones(), textoFont));
                    }
                    document.add(new Paragraph(" "));
                }
            }

            // =============================================
            // FOOTER
            // =============================================
            document.add(new Paragraph(" "));
            LineSeparator lineFooter = new LineSeparator();
            lineFooter.setLineColor(verdeOscuro);
            lineFooter.setLineWidth(1f);
            document.add(lineFooter);

            Paragraph footer = new Paragraph(
                    "Documento generado el " + LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
                            "\nGoblinPark - Sistema de Gestion Ambiental",
                    smallFont
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =====================================================
    // CLASE PARA FONDO CON IMAGEN
    // =====================================================

    class FondoConImagenEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                ClassPathResource fondoResource = new ClassPathResource("static/fondo-hojas.png");
                if (fondoResource.exists()) {
                    Image fondo = Image.getInstance(fondoResource.getURL().toString());
                    fondo.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                    fondo.setAbsolutePosition(0, 0);
                    PdfContentByte cb = writer.getDirectContentUnder();
                    cb.addImage(fondo);
                }
            } catch (Exception e) {
                // Sin fondo si no existe
            }
        }
    }

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

    private void agregarCeldaInfo(PdfPTable table, String label, String value, Font font, Color bgColor) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, new Font(Font.HELVETICA, 9, Font.BOLD)));
        labelCell.setBackgroundColor(bgColor);
        labelCell.setBorder(Rectangle.BOX);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setBackgroundColor(Color.WHITE);
        valueCell.setBorder(Rectangle.BOX);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void agregarCeldaEncabezado(PdfPTable table, String texto, Color bgColor) {
        Font font = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(bgColor);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void agregarCeldaSimple(PdfPTable table, String texto, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private String formatearFecha(java.time.LocalDate fecha) {
        if (fecha == null) return "-";
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String formatearFechaHora(java.time.LocalDateTime fechaHora) {
        if (fechaHora == null) return "-";
        return fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    // =====================================================
// MÉTODO PARA CARGAR ÍCONO DESDE RESOURCES
// =====================================================

    private Image cargarIcono(String nombreArchivo) {
        try {
            ClassPathResource iconoResource = new ClassPathResource("static/" + nombreArchivo);
            if (iconoResource.exists()) {
                Image icono = Image.getInstance(iconoResource.getURL().toString());
                icono.scaleToFit(20, 20);  // Tamaño del ícono en el PDF
                return icono;
            }
        } catch (Exception e) {
            // Si no existe, retornar null
        }
        return null;
    }

// =====================================================
// MÉTODO PARA CREAR TÍTULO CON ÍCONO (PEGADOS)
// =====================================================

    private void agregarTituloConIcono(Document document, String titulo, String nombreIcono, Font font) {
        // Crear párrafo con ícono y texto juntos
        Paragraph parrafoTitulo = new Paragraph();
        parrafoTitulo.setSpacingBefore(10);
        parrafoTitulo.setSpacingAfter(5);

        // Agregar ícono como chunk
        Image icono = cargarIcono(nombreIcono);
        if (icono != null) {
            icono.setAlignment(Element.ALIGN_BOTTOM);
            Chunk chunkIcono = new Chunk(icono, 0, 0, true);
            parrafoTitulo.add(chunkIcono);
            parrafoTitulo.add(Chunk.NEWLINE);  // Salto de línea si es necesario
        }

        // Agregar texto
        parrafoTitulo.add(new Chunk(" " + titulo, font));

        document.add(parrafoTitulo);
    }
}