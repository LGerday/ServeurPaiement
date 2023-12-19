package Serveur.Protocole.Secure;

import Serveur.Protocole.Requete;

import javax.crypto.SecretKey;
import java.security.*;

public class FactureRequeteSecure implements Requete {

    private int idClient;
    private byte[] msg;
    public FactureRequeteSecure(SecretKey sessionKey, int idClient, PrivateKey privateKey){
        this.idClient = idClient;
        try {
            Signature s = Signature.getInstance("SHA1withRSA","BC");
            s.initSign(privateKey);
            String tmp = String.valueOf(idClient);
            s.update(tmp.getBytes());
            msg = s.sign();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public int getIdClient() {
        return idClient;
    }

    public byte[] getMsg() {
        return msg;
    }
}
