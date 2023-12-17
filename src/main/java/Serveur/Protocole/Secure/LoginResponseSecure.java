package Serveur.Protocole.Secure;

import JDBC.BeanJDBC;
import Serveur.Protocole.Reponse;

public class LoginResponseSecure implements Reponse {
    private boolean valide;

    public LoginResponseSecure(boolean v){
        valide = v;
    }

    public boolean isValide() {
        return valide;
    }
}
