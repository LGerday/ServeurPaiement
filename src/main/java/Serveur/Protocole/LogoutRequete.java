package Serveur.Protocole;

public class LogoutRequete implements Requete{

    String msg;

    public LogoutRequete(String msg){

        this.msg = msg;

    }

    public String getMsg(){
        return msg;
    }
}
