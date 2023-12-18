package Serveur.Protocole.Secure;

import Serveur.Protocole.Requete;

import javax.crypto.SecretKey;

public class ArticleRequeteSecure implements Requete {

    private byte[] msg;
    public ArticleRequeteSecure(int idArticle, SecretKey sessionKey){
        msg = CryptData.CryptSymDES(sessionKey,CryptData.StringToByte(String.valueOf(idArticle)));
    }

    public byte[] getMsg() {
        return msg;
    }
}
