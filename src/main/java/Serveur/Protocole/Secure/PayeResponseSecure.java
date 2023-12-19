package Serveur.Protocole.Secure;

import Serveur.Protocole.Reponse;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class PayeResponseSecure implements Reponse {
    private String valide;
    private byte[] Hmac;

    public PayeResponseSecure(boolean v, SecretKey sessionKey){
        if(v)
            valide = "true";
        else
            valide = "false";
        Hmac = generateHmac(sessionKey);

    }
    public boolean isValide() {
        if(valide.equalsIgnoreCase("true"))
            return true;
        else
            return false;
    }

    public byte[] getHmac() {
        return Hmac;
    }

    public String getValide() {
        return valide;
    }

    public byte[] generateHmac(SecretKey sessionKey){
        try {
            Mac hm = Mac.getInstance("HMAC-MD5","BC");
            hm.init(sessionKey);
            hm.update(valide.getBytes());
            return hm.doFinal();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
