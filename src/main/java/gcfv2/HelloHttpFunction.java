package gcfv2;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.itextpdf.html2pdf.HtmlConverter;

import java.io.OutputStream;

public class HelloHttpFunction implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        // Lee el HTML del cuerpo
        String html = request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);

        // Configura la respuesta como PDF
        response.setContentType("application/pdf");

        // Convierte HTML a PDF directamente en el output stream
        OutputStream outputStream = response.getOutputStream();
        HtmlConverter.convertToPdf(html, outputStream);
    }
}
