package Serveur.Protocole.Secure;

import Serveur.Protocole.Facture;
import Serveur.Protocole.Reponse;

import javax.crypto.SecretKey;
import java.util.ArrayList;

public class FactureResponseSecure implements Reponse {


    private byte[] msg;

    public byte[] getMsg() {
        return msg;
    }

    public FactureResponseSecure(ArrayList<Facture>factures, SecretKey sessionKey){
        String tmp = null;
        if(!factures.isEmpty())
        {
            System.out.println("Création facture array");
            tmp = createStringArray(factures);
        }
        else
            tmp = "nothing$";

        msg = CryptData.CryptSymDES(sessionKey,CryptData.StringToByte(tmp));
    }

    public String createStringArray(ArrayList<Facture>factures){
        String msg ="";
        for(Facture fac : factures){
            msg = msg + fac.toString()+"$";
        }
        System.out.println("facture string : "+ msg);
        return msg;
    }
}
