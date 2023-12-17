package Serveur.Protocole.UnSecure;

import Serveur.Protocole.Requete;

public class ArticleRequete implements Requete {

    private int idFacture;

    public ArticleRequete(int facture){
        idFacture = facture;
    }

    public int getIdFacture(){return idFacture;}
}
