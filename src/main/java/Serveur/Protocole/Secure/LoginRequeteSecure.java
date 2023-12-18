package Serveur.Protocole.Secure;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Date;

import Serveur.Protocole.Article;
import Serveur.Protocole.Facture;
import Serveur.Protocole.Requete;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class LoginRequeteSecure implements Requete {
    private byte[] message;

    public LoginRequeteSecure(String login,String password,SecretKey key){
        message = CryptData.CryptSymDES(key,CryptData.StringToByte(login+";"+password+";"));
    }

    public byte[] getMessage() {
        return message;
    }
}
