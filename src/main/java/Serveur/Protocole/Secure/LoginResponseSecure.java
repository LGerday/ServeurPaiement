package Serveur.Protocole.Secure;

import JDBC.BeanJDBC;
import Serveur.Protocole.Reponse;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;

public class LoginResponseSecure implements Reponse {
    private boolean valide;
    private byte[] data;

    public LoginResponseSecure(boolean v, SecretKey sessionKey, PublicKey publicKey){
        valide = v;
        if(v){
            System.out.println("public key vespaps : "+publicKey);
            this.data = CryptData.CryptAsymRSA(publicKey,sessionKey.getEncoded());
        }
    }

    public byte[] getData() {
        return data;
    }

    public boolean isValide() {
        return valide;
    }

}
