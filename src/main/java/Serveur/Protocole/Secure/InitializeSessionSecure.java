package Serveur.Protocole.Secure;

import Serveur.Protocole.Requete;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

public class InitializeSessionSecure implements Requete {

    private byte[] sessionKeyCrypt;

    public InitializeSessionSecure(SecretKey sessionKey){

        this.sessionKeyCrypt = CryptData.CryptAsymRSA(CryptData.RecupereClePubliqueServeur(),sessionKey.getEncoded());
    }
    public byte[] getSessionKeyCrypt() {
        return sessionKeyCrypt;
    }
}
