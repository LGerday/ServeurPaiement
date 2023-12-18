package Serveur.Protocole.Secure;

import javax.crypto.*;
import java.io.*;
import java.security.*;


public class CryptData {
    public static byte[] CryptSymDES(SecretKey cle, byte[] data)
    {
        Cipher chiffrementE = null;
        try {
            chiffrementE = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");
            chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
            return chiffrementE.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }
    public static byte[] DecryptSymDES(SecretKey cle,byte[] data)
    {
        try{
            Cipher chiffrementD = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");
            chiffrementD.init(Cipher.DECRYPT_MODE, cle);
            return chiffrementD.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException |
        IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
    }

    }
    public static byte[] CryptAsymRSA(PublicKey cle,byte[] data)
    {
        try{
            Cipher chiffrementE = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
            chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
            return chiffrementE.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException |
        IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
    public static byte[] DecryptAsymRSA(PrivateKey cle,byte[] data)
    {
        try{
            Cipher chiffrementD = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
            chiffrementD.init(Cipher.DECRYPT_MODE, cle);
            return chiffrementD.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException |
        IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
    public static PublicKey RecupereClePubliqueServeur()
    {
        PublicKey cle = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/main/java/ServerPublicKey.ser"));
            cle = (PublicKey) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cle;
    }
    public static PrivateKey RecupereClePriveeServeur()
    {
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/main/java/ServerPrivateKey.ser"));
            PrivateKey cle = (PrivateKey) ois.readObject();
            ois.close();
            return cle;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] StringToByte(String msg){

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF(msg);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static String ByteToString(byte[] msg){

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(msg);
            DataInputStream dis = new DataInputStream(bais);
            return dis.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
