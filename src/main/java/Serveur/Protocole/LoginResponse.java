package Serveur.Protocole;

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