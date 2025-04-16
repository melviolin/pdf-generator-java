package gcfv2;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.font.FontProvider;

import java.io.OutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.nio.file.Files;

public class HelloHttpFunction implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        // Leer HTML desde el cuerpo
        String html = request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);

        // Configurar la respuesta
        response.setContentType("application/pdf");
        OutputStream outputStream = response.getOutputStream();

        // Cargar la fuente desde la carpeta 'fonts'
        FontProvider fontProvider = new FontProvider();
        fontProvider.addFont("fonts/ARIAL.ttf");

        ConverterProperties properties = new ConverterProperties();
        properties.setFontProvider(fontProvider);
        properties.setCharset("UTF-8");

        // Convertir HTML a PDF
        HtmlConverter.convertToPdf(html, outputStream, properties);
    }
}
