package com.tgm.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.tgm.model.Oficio;
import java.io.FileOutputStream;

public class ReporteGenerador {

    public void generarPdfOficio(Oficio oficio, String rutaArchivo) {

        Document documento = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            //Flujo de escritura
            PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
            documento.open();

            //DISEÑO DEL DOCUMENTO

            //Encabezado
            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph titulo = new Paragraph("TALLERES GRÁFICOS DE MÉXICO", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            documento.add(new Paragraph(" "));

            //Datos del Oficio
            Font fuenteNegrita = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Paragraph info = new Paragraph("Oficio: " + oficio.getFolio(), fuenteNegrita);
            info.setAlignment(Element.ALIGN_RIGHT);
            documento.add(info);

            documento.add(new Paragraph(" "));

            //Asunto
            documento.add(new Paragraph("Asunto: " + oficio.getAsunto(), fuenteNegrita));
            documento.add(new Paragraph(" "));

            //Cuerpo del texto
            Paragraph cuerpo = new Paragraph(oficio.getCuerpo());
            cuerpo.setAlignment(Element.ALIGN_JUSTIFIED);
            documento.add(cuerpo);

            documento.add(new Paragraph("\n\n\n"));

            //Firma
            Paragraph firma = new Paragraph("_______________________\nFIRMA DE AUTORIZACIÓN");
            firma.setAlignment(Element.ALIGN_CENTER);
            documento.add(firma);

            documento.close();

            System.out.println("PDF generado con éxito en: " + rutaArchivo);
        } catch (Exception e) {
            System.out.println("Error al generar PDF: " + e.getMessage());
        }
    }
}
