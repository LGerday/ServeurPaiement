package Web;

import JDBC.BeanJDBC;
import Serveur.Protocole.Article;
import Serveur.Protocole.Facture;
import Serveur.Protocole.UnSecure.FactureResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WebAPI {

    private static BeanJDBC bean = new BeanJDBC();
    private static ArrayList<Article> myArticles = new ArrayList<Article>();
    public static void main(String[] args) throws IOException
    {
        System.out.println("API Rest demarree...");
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/api/article", new TaskHandler());
        server.start();
    }

    static class TaskHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange exchange) throws IOException
        {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET"))
            {
                System.out.println("--- Requête GET reçue (obtenir la liste) ---");
                // Récupérer la liste des tâches au format JSON
                myArticles = loadArticleFromBD();
                String response = convertArticleToJson();
                sendResponse(exchange, 200, response);
            }
            else if (requestMethod.equalsIgnoreCase("POST"))
            {
                System.out.println("--- Requête POST reçue (ajout) ---");
                // Ajouter une nouvelle tâche
                String requestBody = exchange.getRequestURI().getQuery();
                System.out.println("requestNody = " + requestBody);
                Map<String, String> params = new HashMap<>();
                String[] pairs = requestBody.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        String key = keyValue[0];
                        String value = keyValue[1];
                        params.put(key, value);
                    }
                }

                // Utilisation des paramètres récupérés
                String id = params.get("id");
                String stock = params.get("stock");
                String price = params.get("prix");
                updateArticle(Integer.parseInt(id),Double.parseDouble(price),Integer.parseInt(stock));
                updateDB(Integer.parseInt(id),Double.parseDouble(price),Integer.parseInt(stock));
                sendResponse(exchange, 201, "Oui");
            }
            else sendResponse(exchange, 405, "Methode non autorisee");
        }
    }
    private static void sendResponse(HttpExchange exchange, int statusCode, String
            response) throws IOException
    {
        System.out.println("Envoi de la réponse (" + statusCode + ") : --" + response + "--");
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static String readRequestBody(HttpExchange exchange) throws IOException
    {
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            requestBody.append(line);
        }
        reader.close();
        return requestBody.toString();
    }
    private static Map<String, String> parseQueryParams(String query)
    {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null)
        {
            String[] params = query.split("&");
            for (String param : params)
            {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2)
                {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }
    private static String convertArticleToJson()
    {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < myArticles.size(); i++)
        {
            json.append("{\"Id\": ").append(i + 1).append(", \"Nom\":\"").append(myArticles.get(i).getName()).append("\", \"Prix\":").append(myArticles.get(i).getPrice()).append(", \"Stock\":").append(myArticles.get(i).getQuantity()).append("}");
            if (i < myArticles.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
    private static void addArticle(Article article)
    {
        myArticles.add(article);
    }
    private static void updateArticle(int idArticle,double price,int stock)
    {
        if (idArticle >= 1 && idArticle <= myArticles.size())
        {
            myArticles.get(idArticle-1).setPrice(price);
            myArticles.get(idArticle-1).setQuantity(stock);
        }
    }
    private static void deleteArticle(int articleID)
    {
        if (articleID >= 1 && articleID <= myArticles.size())
        {
            myArticles.remove(articleID - 1);
        }
    }

    private static ArrayList<Article> loadArticleFromBD() {
        ArrayList<Article> articles = new ArrayList<>();
        String query = "select * from articles";
        try {
            bean.execute(query);
            ResultSet rs = bean.getRs();
            if (rs != null) {
                while (rs.next()) {

                    int idArticle = rs.getInt("id");
                    String intitule = rs.getString("intitule");
                    int stock = rs.getInt("stock");
                    Double price = rs.getDouble("prix");
                    articles.add(new Article(stock,intitule,price,0.0));
                }
            } else {

                System.err.println("Le ResultSet est null. Aucun résultat trouvé.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return articles;
    }
        private static void updateDB(int idArticle,double price,int stock){

        String query = "update articles set stock = '"+stock+"' WHERE id = '"+idArticle+"'";
        String query2 = "update articles set prix = '"+price+"' WHERE id = '"+idArticle+"'";
        try{
            bean.execute(query);
            bean.execute(query2);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
