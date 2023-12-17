package Serveur.Protocole.UnSecure;

import Serveur.Protocole.Requete;

public class LoginRequete implements Requete
{
    private String login;
    private String password;
    public LoginRequete(String l,String p) {
        login = l;
        password = p;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
}
