package Serveur.Protocole.UnSecure;

import JDBC.BeanJDBC;
import Serveur.FinConnexionException;
import Serveur.Protocole.*;

import java.net.Socket;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class VESPAP implements Protocole {

    private BeanJDBC bean;
    private HashMap<String,Socket> clientsConnectes;
    private Logger logger;


    public VESPAP(Logger log){
        logger = log;
        clientsConnectes = new HashMap<>();
        bean = new BeanJDBC();
    }
    @Override
    public String getNom() {
        return "VESPAP";
    }

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {
        if (requete instanceof LoginRequete)
            return TraiteRequeteLOGIN((LoginRequete) requete, socket);
        if (requete instanceof FactureRequete)
            return TraiteRequeteFACTURE((FactureRequete) requete, socket);
        if( requete instanceof PayeRequete)
            return TraiteRequetePaye((PayeRequete) requete,socket);
        if(requete instanceof LogoutRequete)
            return TraiteRequeteLOGOUT((LogoutRequete) requete,socket);
        if(requete instanceof ArticleRequete)
            return TraiteRequeteArticle((ArticleRequete) requete,socket);

        return null;
    }

    private synchronized LoginResponse TraiteRequeteLOGIN(LoginRequete requete, Socket socket) throws FinConnexionException {
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());
        String query = "select * from employes where username = '" + requete.getLogin()+"'";
        String password = null;
        try {
            bean.execute(query);

            // Récupère le ResultSet et affiche les résultats
            ResultSet rs = bean.getRs();
            if (rs != null) {
                while (rs.next()) {
                    // Récupère les colonnes par leur nom (ajuste les noms selon ta structure de base de données)
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    password = rs.getString("password");
                }
            } else {
                // Gère le cas où rs est null
                System.err.println("Le ResultSet est null. Aucun résultat trouvé.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (password != null)
            if (password.equals(requete.getPassword())){
                String ipPortClient = socket.getInetAddress().getHostAddress() + "/" +
                        socket.getPort();
                logger.Trace(requete.getLogin() + " correctement loggé de " + ipPortClient);
                clientsConnectes.put(requete.getLogin(), socket);
                System.out.println("Login valide");
                return new LoginResponse(true);
            }
        logger.Trace(requete.getLogin() + " --> erreur de login");
        return new LoginResponse(false);
    }

    private synchronized LogoutResponse TraiteRequeteLOGOUT(LogoutRequete requete, Socket socket) throws FinConnexionException {
        logger.Trace("RequeteLOGOUT reçue de "+socket.getInetAddress().getHostAddress() + "/" +
                socket.getPort());
        LogoutResponse rep = new LogoutResponse(true);

        return rep;
    }
    private synchronized FactureResponse TraiteRequeteFACTURE(FactureRequete requete, Socket socket) throws FinConnexionException {
        logger.Trace("RequeteFacture : reception facture du client " + requete.getIdClient());
        String query = "select * from factures where idClient = '" + requete.getIdClient()+"' AND paye = '0'";
        FactureResponse rep = new FactureResponse();
        try {
            bean.execute(query);
            ResultSet rs = bean.getRs();
            if (rs != null) {
                while (rs.next()) {

                    int idFacture = rs.getInt("ID");
                    int idClient = rs.getInt("idClient");
                    Date sqlDate = rs.getDate("date");
                    Double montant = rs.getDouble("montant");
                    rep.Factures.add(new Facture(idFacture,idClient,sqlDate,montant));
                }
            } else {

                System.err.println("Le ResultSet est null. Aucun résultat trouvé.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.Trace(rep.Factures.size() + " facture(s) recuperer avec succès");
        return rep;

    }
    private synchronized PayeResponse TraiteRequetePaye(PayeRequete requete, Socket socket) throws FinConnexionException {
        logger.Trace("RequetePaye reçue de "+requete.getName() + " pour la facture : "+ requete.getFacture());
        PayeResponse rep = new PayeResponse(checkLuhn(requete.getCardNumber()));
        if(checkLuhn(requete.getCardNumber())){
            rep.setCardError("Carte Valide");
            logger.Trace("Paiement valide pour facture : "+requete.getFacture());
            String query = "update factures set paye = '1' WHERE ID = '"+requete.getFacture()+"'";
            try{
                bean.execute(query);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else {
            logger.Trace("Paiement invalide pour facture : "+requete.getFacture());
            rep.setCardError("Numero de carte invalide");
        }
        return rep;
    }
    private synchronized ArticleResponse TraiteRequeteArticle(ArticleRequete requete, Socket socket) throws FinConnexionException {
        logger.Trace("ArticleRequete recu pour facture : "+requete.getIdFacture());
        String query = "select * from ventes where idFacture = '" + requete.getIdFacture()+"'";
        ArticleResponse rep = new ArticleResponse();
        Double price =0.0;
        Double total =0.0;
        String name = "";
        try {
            bean.execute(query);
            ResultSet rs = bean.getRs();
            if (rs != null) {
                while (rs.next()) {
                    int stock = rs.getInt("quantite");
                    int article = rs.getInt("idArticle");
                    System.out.println("Requete ventes : "+article + " "+ stock);
                    query = "select * from articles where id ='"+article+"'";
                    bean.execute(query);
                    ResultSet rs2 = bean.getRs();
                    if (rs2 != null) {
                        while (rs2.next()) {
                            // Récupère les colonnes par leur nom (ajuste les noms selon ta structure de base de données)
                            name = rs2.getString("intitule");
                            price = rs2.getDouble("prix");
                            System.out.println("Requete article : "+name + " "+ price);
                        }
                    } else {
                        // Gère le cas où rs est null
                        System.err.println("Le ResultSet est null. Aucun résultat trouvé.");
                    }

                    total = price * stock;

                    rep.Articles.add(new Article(stock,name,price,total));
                }
            } else {

                System.err.println("Le ResultSet est null. Aucun résultat trouvé.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rep;
    }
    public boolean checkLuhn(String cardNo)
    {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {

            int d = cardNo.charAt(i) - '0';

            if (isSecond)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }
}
