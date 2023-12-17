package Serveur.Protocole.UnSecure;

import Serveur.Protocole.Reponse;

public class LogoutResponse implements Reponse {

    private boolean valide;

    public LogoutResponse(boolean v){
        valide = v;

    }

    public boolean getValide(){
        return valide;
    }

}
