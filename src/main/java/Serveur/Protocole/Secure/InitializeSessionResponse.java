package Serveur.Protocole.Secure;

import Serveur.Protocole.Reponse;

public class InitializeSessionResponse implements Reponse {

    private boolean valide;

    public InitializeSessionResponse(boolean v){
        valide = v;
    }

    public boolean isValide() {
        return valide;
    }
}
