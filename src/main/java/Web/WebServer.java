package Web;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {
    public static void main(String[] args)
    {
        HttpServer serveur = null;
        try
        {
            //initialisation du serveur ainsi que ses handler
            serveur = HttpServer.create(new InetSocketAddress(8080),0);
            serveur.createContext("/",new HandlerHtml());
            serveur.createContext("/css",new HandlerCss());
            serveur.createContext("/js",new HandlerJs());
            serveur.createContext("/images",new HandlerImg());
            System.out.println("Demarrage du serveur HTTP...");
            serveur.start();
            System.out.println("Serveur HTTP démarré sur le port 8080 !");
        }
        catch (IOException e)
        {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}
