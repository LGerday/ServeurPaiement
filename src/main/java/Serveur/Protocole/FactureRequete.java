package Serveur.Protocole;

public class FactureRequete implements Requete{

    private int IdClient;

    public FactureRequete(int id){
        IdClient = id;
    }

    public int getIdClient(){
        return IdClient;
    }
}
