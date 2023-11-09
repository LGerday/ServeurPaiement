package Serveur.Protocole;

import JDBC.BeanJDBC;
import Serveur.FinConnexionException;

import java.net.Socket;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class VESPAP implements Protocole{

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
        if (requete instanceof FactureRequete) {
            return TraiteRequeteFACTURE((FactureRequete) requete, socket);
        }


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

                    // Affiche les résultats
                    System.out.println("ID: " + id + ", Username: " + username + ", Password: " + password);
                }
            } else {
                // Gère le cas où rs est null
                System.err.println("Le ResultSet est null. Aucun résultat trouvé.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (password != null)
            if (password.equals(requete.getPassword())) {
                String ipPortClient = socket.getInetAddress().getHostAddress() + "/" +
                        socket.getPort();
                logger.Trace(requete.getLogin() + " correctement loggé de " + ipPortClient);
                clientsConnectes.put(requete.getLogin(), socket);
                return new LoginResponse(true);
            }
        logger.Trace(requete.getLogin() + " --> erreur de login");
        throw new FinConnexionException(new LoginResponse(false));
    }
    private synchronized FactureResponse TraiteRequeteFACTURE(FactureRequete requete, Socket socket) throws FinConnexionException {
        logger.Trace("RequeteFacture reçue de " + requete.getIdClient());
        String query = "select * from factures where idClient = '" + requete.getIdClient()+"'";
        FactureResponse rep = new FactureResponse();
        try {
            bean.execute(query);

            // Récupère le ResultSet et affiche les résultats
            ResultSet rs = bean.getRs();
            if (rs != null) {
                while (rs.next()) {
                    // Récupère les colonnes par leur nom (ajuste les noms selon ta structure de base de données)
                    int idFacture = rs.getInt("idFacture");
                    Date sqlDate = rs.getDate("date");
                    Double montant = rs.getDouble("montant");

                    // Affiche les résultats
                    System.out.println("ID: " + idFacture + ", Date: " + sqlDate + ", montant: " + montant);
                    rep.Factures.add(new Facture(idFacture,sqlDate,montant));
                }
            } else {
                // Gère le cas où rs est null
                System.err.println("Le ResultSet est null. Aucun résultat trouvé.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.Trace("Recuperation facture client : "+requete.getIdClient() );
        return rep;

    }


}
