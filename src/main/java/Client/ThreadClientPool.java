package Client;

import Serveur.FileAttente;
import Serveur.Protocole.Logger;
import Serveur.Protocole.Protocole;

import java.io.IOException;

public class ThreadClientPool extends ThreadClient {
    private FileAttente connexionsEnAttente;
    public ThreadClientPool(Protocole protocole, FileAttente file, ThreadGroup groupe, Logger logger) throws IOException {
        super(protocole, groupe, logger);
        connexionsEnAttente = file;
    }
    @Override
    public void run() {
        logger.Trace("TH Client (Pool) démarre...");
        boolean interrompu = false;
        while (!interrompu) {
            try {
                logger.Trace("Attente d'une connexion...");
                csocket = connexionsEnAttente.getConnexion();
                logger.Trace("Connexion prise en charge.");
                super.run();
            } catch (InterruptedException ex) {
                logger.Trace("Demande d'interruption...");
                interrompu = true;
            }
        }
        logger.Trace("TH Client (Pool) se termine.");
    }
}
