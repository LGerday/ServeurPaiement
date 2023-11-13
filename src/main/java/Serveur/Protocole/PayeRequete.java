package Serveur.Protocole;

public class PayeRequete implements Requete{

    private String CardNumber;
    private String Name;
    private int Facture;

    public PayeRequete(String card,String name,int facture){
        CardNumber = card;
        Name = name;
        Facture = facture;
    }

    public String getCardNumber(){
        return CardNumber;
    }
    public String getName()
    {
        return Name;
    }
    public int getFacture(){
        return Facture;
    }
}
