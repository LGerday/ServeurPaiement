package Serveur.Protocole.Secure;

import Serveur.Protocole.Reponse;

public class PayeResponseSecure implements Reponse {
    private boolean valide;

    public PayeResponseSecure(boolean v){
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }
}
