package gcfv2;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;

import java.io.InputStream;
import java.io.OutputStream;

public class HelloHttpFunction implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        String html = request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);

        // Cargar la fuente personalizada desde resources
        FontProvider fontProvider = new FontProvider();
        try (InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/ARIAL.ttf")) {
            if (fontStream != null) {
                fontProvider.addFont(fontStream);
            } else {
                throw new RuntimeException("Fuente no encontrada en /resources/fonts/ARIAL.ttf");
            }
        }

        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setFontProvider(fontProvider);
        converterProperties.setCharset("UTF-8");

        response.setContentType("application/pdf");

        OutputStream outputStream = response.getOutputStream();
        HtmlConverter.convertToPdf(html, outputStream, converterProperties);
    }
}
