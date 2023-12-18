package Serveur.Protocole.Secure;

import JDBC.BeanJDBC;
import Serveur.Protocole.Reponse;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class LoginResponseSecure implements Reponse {
    private boolean valide;
    private SecretKey sessionKey;

    public SecretKey getSessionKey(){
        return sessionKey;
    }

    public LoginResponseSecure(boolean v){
        valide = v;
        if(v)
            sessionKey = generateSessionKey();
    }

    public boolean isValide() {
        return valide;
    }

    public SecretKey generateSessionKey(){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("DES","BC");
            keyGen.init(new SecureRandom());
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }

    }
}
