package Serveur.Protocole;

public class PayeResponse implements Reponse {

    private String CardError;
    private boolean CardValide;

    private int Facture;

    public PayeResponse(boolean cardValide){
        CardError = "";
        CardValide = cardValide;
        Facture = -1;
    }

    public void setCardError(String error){
        CardError = error;
    }
    public boolean IsCardValide(){
        return CardValide;
    }

    public String getCardError(){
        return CardError;
    }

    public void setFacture(int facture){
        Facture = facture;
    }
    public int getFacture(){
        return Facture;
    }
}
