package Web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HandlerImg implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        String requestPath = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();
        System.out.print("HandlerImg (methode " + requestMethod + ") = " + requestPath + " --> ");

        if (requestPath.endsWith(".jpg")) {
            String fichier = "src/main/java/Web/HtmlFile/" + requestPath;
            File file = new File(fichier);
            if (file.exists()) {
                exchange.sendResponseHeaders(200, file.length());
                exchange.getResponseHeaders().set("Content-Type", "text/css");
                OutputStream os = exchange.getResponseBody();
                Files.copy(file.toPath(), os);
                os.close();
                System.out.println("OK");
            } else Erreur404(exchange);
        } else Erreur404(exchange);
    }

    private void Erreur404(HttpExchange exchange) throws IOException {
        String reponse = "Fichier image introuvable !!!";
        exchange.sendResponseHeaders(404, reponse.length());
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        OutputStream os = exchange.getResponseBody();
        os.write(reponse.getBytes());
    }
}
