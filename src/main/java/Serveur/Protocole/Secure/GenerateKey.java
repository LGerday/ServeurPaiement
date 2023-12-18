package Serveur.Protocole.Secure;

import java.io.*;
import java.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
public class GenerateKey {
    public static void main(String args[]){
        Security.addProvider(new BouncyCastleProvider());
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA","BC");
            keyGen.initialize(512,new SecureRandom());
            KeyPair keyPair = keyGen.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            System.out.println("Public : "+ publicKey);
            System.out.println("Private : "+ privateKey);
            ObjectOutputStream oss1  = new ObjectOutputStream(new FileOutputStream("src/main/java/ServerPublicKey.ser"));
            oss1.writeObject(publicKey);
            ObjectOutputStream oss2  = new ObjectOutputStream(new FileOutputStream("src/main/java/ServerPrivateKey.ser"));
            oss2.writeObject(privateKey);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
