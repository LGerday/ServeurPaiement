package Serveur.Protocole;

import Serveur.FinConnexionException;

import java.net.Socket;
import java.util.HashMap;

public class VESPAP implements Protocole{

    private HashMap<String,Socket> clientsConnectes;
    private Logger logger;
    @Override
    public String getNom() {
        return "VESPAP";
    }

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {
        if (requete instanceof LoginRequete) return TraiteRequeteLOGIN((LoginRequete) requete, socket);

        return null;
    }

    private synchronized LoginResponse TraiteRequeteLOGIN(LoginRequete requete, Socket socket) throws FinConnexionException {
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());
        String password = null;
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
}
