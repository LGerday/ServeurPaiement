package Serveur.Protocole.Secure;

import Serveur.Protocole.Requete;

import javax.crypto.SecretKey;

public class FactureRequeteSecure implements Requete {

    private byte[] msg;
    public FactureRequeteSecure(SecretKey sessionKey,int idClient){
        msg = CryptData.CryptSymDES(sessionKey,CryptData.StringToByte(String.valueOf(idClient)));
    }

    public byte[] getMsg() {
        return msg;
    }
}
