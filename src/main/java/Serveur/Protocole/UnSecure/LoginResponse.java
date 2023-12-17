package Serveur.Protocole.UnSecure;

import Serveur.Protocole.Reponse;

public class LoginResponse implements Reponse
{
    private boolean valide;
    public LoginResponse(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }
}