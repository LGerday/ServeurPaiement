package Serveur.Protocole.UnSecure;

import Serveur.Protocole.Requete;

public class LogoutRequete implements Requete {

    String username;

    public LogoutRequete(String msg){

        this.username = msg;

    }

    public String getMsg(){
        return username;
    }
}
