package Serveur.Protocole.Secure;

import Serveur.Protocole.Requete;

import javax.crypto.SecretKey;

public class PayeRequeteSecure implements Requete {


    private byte[] data;
    public PayeRequeteSecure(String card, String name, int facture, SecretKey sessionKey){
        data = CryptData.CryptSymDES(sessionKey,CryptData.StringToByte(createStringPaye(card,name,facture)));
    }

    public byte[] getData(){
        return data;
    }
    public String createStringPaye(String card,String name,int facture){
        return card+";"+name+";"+facture+";";
    }
}
