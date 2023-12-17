package Serveur.Protocole.Secure;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Date;
import Serveur.Protocole.Requete;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class LoginRequeteSecure implements Requete {
    private String login;
    private long temps;
    private double alea;
    private byte[] digest;

    public LoginRequeteSecure(String login,String password){
        this.login = login;
        this.temps = new Date().getTime();
        this.alea = Math.random();
        Security.addProvider(new BouncyCastleProvider());

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
            md.update(login.getBytes());
            md.update(password.getBytes());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeLong(temps);
            dos.writeDouble(alea);
            md.update(baos.toByteArray());
            digest = md.digest();
            baos.close();
            dos.close();

        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getLogin(){
        return login;
    }

    public long getTemps() {
        return temps;
    }

    public double getAlea() {
        return alea;
    }

    public byte[] getDigest(){
        return digest;
    }
}
