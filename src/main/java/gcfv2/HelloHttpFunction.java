package gcfv2;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HelloHttpFunction implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        // Lee el HTML del cuerpo
        String html = request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);

        // Configura la respuesta como PDF
        response.setContentType("application/pdf");

        // Ruta de la fuente Arial
        String fontPath = "src/main/resources/fonts/ARIAL.ttf"; // Ruta de la fuente Arial
        Font customFont = FontFactory.getFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        // Crear el documento PDF
        Document document = new Document();
        OutputStream outputStream = response.getOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // Agregar contenido usando la fuente personalizada
        Paragraph paragraph = new Paragraph(html, customFont);
        document.add(paragraph);

        // Cerrar el documento
        document.close();
    }
}
