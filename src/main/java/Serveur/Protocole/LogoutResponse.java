package Serveur.Protocole;

public class LogoutResponse implements Requete{

    private boolean valide;

    public LogoutResponse(boolean v){
        valide = v;

    }

    public boolean getValide(){
        return valide;
    }

}
