package Serveur.Protocole.Secure;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Date;

import Serveur.Protocole.Requete;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class LoginRequeteSecure implements Requete {
    private String login;
    private long temps;
    private double alea;
    private PublicKey publicKey;
    private byte[] digest;

    public LoginRequeteSecure(String login, String password, PublicKey publicKey) {
        Security.addProvider(new BouncyCastleProvider());
        this.login = login;
        this.temps = new Date().getTime();
        this.alea = Math.random();
        digest = createDigest(password);
        System.out.println("public key requete : "+publicKey);
        this.publicKey = publicKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] createDigest(String password) throws RuntimeException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1", "BC");
            md.update(login.getBytes());
            md.update(password.getBytes());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeLong(temps);
            dos.writeDouble(alea);
            md.update(baos.toByteArray());
            digest = md.digest();
            return digest;

        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLogin() {
        return login;
    }

    public byte[] getDigest() {
        return digest;
    }

    public boolean VerifyPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1", "BC");
            md.update(login.getBytes());
            md.update(password.getBytes());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeLong(temps);
            dos.writeDouble(alea);
            md.update(baos.toByteArray());
            byte[] digestLocal = md.digest();
            return MessageDigest.isEqual(digest, digestLocal);
        } catch (NoSuchAlgorithmException | IOException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }
}