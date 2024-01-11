package Serveur.Protocole.Secure;

import JDBC.BeanJDBC;
import Serveur.FinConnexionException;
import Serveur.Protocole.*;
import Serveur.Protocole.UnSecure.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class VESPAPS implements Protocole {

    private BeanJDBC bean;
    private HashMap<String,Socket> clientsConnectes;
    private Logger logger;
    private SecretKey sessionKey;
    private PublicKey publicKey;


    public VESPAPS(Logger log){
        logger = log;
        clientsConnectes = new HashMap<>();
        bean = new BeanJDBC();
        Security.addProvider(new BouncyCastleProvider());
    }
    @Override
    public String getNom() {
        return "VESPAPS";
    }

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {
        if (requete instanceof LoginRequeteSecure)
            return TraiteRequeteSecureLOGIN((LoginRequeteSecure) requete, socket);
       /* if (requete instanceof InitializeSessionSecure)
            return TraiteInitialize((InitializeSessionSecure) requete, socket);*/
        if(requete instanceof FactureRequeteSecure)
            return TraiteRequeteFACTURESecure((FactureRequeteSecure) requete,socket);
        if(requete instanceof PayeRequeteSecure)
            return TraiteRequetePayeSecure((PayeRequeteSecure) requete,socket);
        if(requete instanceof LogoutRequete)
            return TraiteRequeteLOGOUT((LogoutRequete) requete,socket);
        if(requete instanceof ArticleRequete)
            return TraiteRequeteArticle((ArticleRequete) requete,socket);

        return null;
    }

    private synchronized LoginResponseSecure TraiteRequeteSecureLOGIN(LoginRequeteSecure requete, Socket socket) throws FinConnexionException {
        logger.Trace("RequeteSecureLOGIN reçue de " );
        String username = requete.getLogin();
        String usernameBd,passwordBd = null;
        String query = "select * from employes where username = '" + username+"'";
        try {
            bean.execute(query);
            ResultSet rs = bean.getRs();
            if (rs != null) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    usernameBd = rs.getString("username");
                    passwordBd = rs.getString("password");
                }
            } else {
                System.err.println("Le ResultSet est null. Aucun résultat trouvé.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (passwordBd != null) {
            if(requete.VerifyPassword(passwordBd))
            {
                System.out.println("Comparaison digest login -> correct");
                String ipPortClient = socket.getInetAddress().getHostAddress() + "/" +
                        socket.getPort();
                logger.Trace(username + " correctement loggé de " + ipPortClient);
                clientsConnectes.put(username, socket);
                sessionKey = generateSessionKey();
                publicKey = requete.getPublicKey();
                return new LoginResponseSecure(true,sessionKey, requete.getPublicKey());
            }
        }

        System.out.println("Comparaison digest login -> incorrect");
        logger.Trace(username + " --> erreur de login");
        return new LoginResponseSecure(false,null,null);
    }
    /*private synchronized InitializeSessionResponse TraiteInitialize(InitializeSessionSecure requete, Socket socket) throws FinConnexionException {
        logger.Trace("Requete initialisation reçue de "+socket.getInetAddress().getHostAddress() + "/" +
                socket.getPort());
        byte[] cleSessionDecryptee = CryptData.DecryptAsymRSA(CryptData.RecupereClePriveeServeur(),requete.getSessionKeyCrypt());
        sessionKey = new SecretKeySpec(cleSessionDecryptee,"DES");
        return new InitializeSessionResponse(true);

    }*/
    private synchronized LogoutResponse TraiteRequeteLOGOUT(LogoutRequete requete, Socket socket) throws FinConnexionException {
        logger.Trace("RequeteLOGOUT reçue de "+socket.getInetAddress().getHostAddress() + "/" +
                socket.getPort());

        return new LogoutResponse(true);
    }
    private synchronized FactureResponseSecure TraiteRequeteFACTURESecure(FactureRequeteSecure requete, Socket socket) throws FinConnexionException {
        System.out.println("Requete facture secure");
        Signature s = null;
        boolean testSignature;
        try {
            s = Signature.getInstance("SHA1withRSA","BC");
            s.initVerify(publicKey);
            String tmp = String.valueOf(requete.getIdClient());
            s.update(tmp.getBytes());
            testSignature = s.verify(requete.getMsg());
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }

        if(testSignature)
        {
            System.out.println("Signature validé");
            logger.Trace("RequeteFacture : reception facture du client " + requete.getIdClient());
            String query = "select * from factures where idClient = '" +requete.getIdClient()+"' AND paye = '0'";
            ArrayList<Facture> MyFactures = new ArrayList<Facture>();
            try {
                bean.execute(query);
                ResultSet rs = bean.getRs();
                if (rs != null) {
                    while (rs.next()) {

                        int idFacture = rs.getInt("ID");
                        int idClient = rs.getInt("idClient");
                        Date sqlDate = rs.getDate("date");
                        Double montant = rs.getDouble("montant");
                        MyFactures.add(new Facture(idFacture,idClient,sqlDate,montant));
                    }
                } else {

                    System.err.println("Le ResultSet est null. Aucun résultat trouvé.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            logger.Trace(MyFactures.size() + " facture(s) recuperer avec succès");
            return new FactureResponseSecure(MyFactures,sessionKey);
        }
        else
        {
            System.out.println("Signature pas validé");
            return new FactureResponseSecure(new ArrayList<>(),sessionKey);
        }

    }
    private synchronized PayeResponseSecure TraiteRequetePayeSecure(PayeRequeteSecure requete, Socket socket) throws FinConnexionException {
        byte[] msgDecrypt = CryptData.DecryptSymDES(sessionKey,requete.getData());
        String msg = CryptData.ByteToString(msgDecrypt);
        String[] msgSplit = msg.split(";");
        String Card = msgSplit[0];
        String Name = msgSplit[1];
        int idFacture = Integer.parseInt(msgSplit[2]);
        System.out.println("Requete payement recu avec : "+Card + " : "+ Name+ "  "+ idFacture);
        logger.Trace("RequetePaye reçue de "+Name + " pour la facture : "+ idFacture);
        PayeResponseSecure rep = new PayeResponseSecure(checkLuhn(Card),sessionKey);
        if(checkLuhn(Card)){

            logger.Trace("Paiement valide pour facture : "+idFacture);
            String query = "update factures set paye = '1' WHERE ID = '"+idFacture+"'";
            try{
                bean.execute(query);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else {
            logger.Trace("Paiement invalide pour facture : "+idFacture);
        }
        return rep;
    }
    private synchronized ArticleResponse TraiteRequeteArticle(ArticleRequete requete, Socket socket) throws FinConnexionException {
        logger.Trace("ArticleRequete recu pour facture : "+requete.getIdFacture());
        String query = "select * from ventes where idFacture = '" + requete.getIdFacture()+"'";
        ArticleResponse rep = new ArticleResponse();
        Double price =0.0;
        Double total =0.0;
        String name = "";
        try {
            bean.execute(query);
            ResultSet rs = bean.getRs();
            if (rs != null) {
                while (rs.next()) {
                    int stock = rs.getInt("quantite");
                    int article = rs.getInt("idArticle");
                    System.out.println("Requete ventes : "+article + " "+ stock);
                    query = "select * from articles where id ='"+article+"'";
                    bean.execute(query);
                    ResultSet rs2 = bean.getRs();
                    if (rs2 != null) {
                        while (rs2.next()) {

                            name = rs2.getString("intitule");
                            price = rs2.getDouble("prix");
                        }
                    } else {

                        System.err.println("Le ResultSet est null. Aucun résultat trouvé.");
                    }

                    total = price * stock;

                    rep.Articles.add(new Article(stock,name,price,total));
                }
            } else {

                System.err.println("Le ResultSet est null. Aucun résultat trouvé.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rep;
    }
    private SecretKey generateSessionKey(){
        KeyGenerator cleGen = null;
        Security.addProvider(new BouncyCastleProvider());
        try {
            cleGen = KeyGenerator.getInstance("DES","BC");

        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        cleGen.init(new SecureRandom());
        return cleGen.generateKey();
    }
    public boolean checkLuhn(String cardNo)
    {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {

            int d = cardNo.charAt(i) - '0';

            if (isSecond)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }
}
