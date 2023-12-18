package Client;

import Serveur.Protocole.*;
import Serveur.Protocole.Secure.*;
import Serveur.Protocole.UnSecure.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.sql.Date;
import java.util.Arrays;

public class ClientPaiementUI extends JFrame {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private JTextField idField;
    private JTextField cardNumberField;
    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton paymentButton;
    private JButton actualiserButton;
    private JButton getArticleButton;
    public boolean securePort;
    boolean dataGridChange;
    private SecretKey sessionKey;
    // si true -> datagrid Factures
    // si false -> datagrid Articles

    public ClientPaiementUI() throws IOException {

        super("ClientPaiementUI");
        dialogSecureChoice(this);
        int port;
        if(securePort)
            port = 50001;
        else
            port = 50000;
        sessionKey = generateSessionKey();
        socket = new Socket("127.0.0.1",port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        dataGridChange = true;

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout());
        JLabel usernameLabel = new JLabel("Nom d'utilisateur: ");
        usernameField = new JTextField(10);
        JLabel passwordLabel = new JLabel("Mot de passe: ");
        passwordField = new JPasswordField(10);
        loginButton = new JButton("Se connecter");
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        JButton logoutButton = new JButton("Logout");
        loginPanel.add(logoutButton);
        add(loginPanel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Code à exécuter lors de la fermeture de la fenêtre
                System.out.println("[Close Client] Fermeture Flux & Socket");
                try{
                    ois.close();
                    oos.close();
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(0); // Fermer l'application (à ajuster selon vos besoins)
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("[logoutButton] press");
                try {
                    LogoutRequete requete = new LogoutRequete(usernameField.getText());
                    oos.writeObject(requete);
                    LogoutResponse reponse = (LogoutResponse) ois.readObject();
                    if (reponse.getValide()) {
                        System.out.println("[logoutButton] Logout Valide");
                        logoutButton.setEnabled(false);
                        loginButton.setEnabled(true);
                        actualiserButton.setEnabled(false);
                        paymentButton.setEnabled(false);
                        getArticleButton.setEnabled(false);
                        usernameField.setText("");
                        passwordField.setText("");
                        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                        tableModel.setRowCount(0);
                        cardNumberField.setText("");
                        idField.setText("");
                        nameField.setText("");

                    } else {
                        System.out.println("[logoutButton] Logout Invalide");
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean valide = false;
                if(securePort){
                    valide = LoginSecure();
                }
                else
                {
                    valide = LoginUnsecure();
                }
                if (valide) {
                    System.out.println("[loginButton] Login Valide");
                    createDialoge("Vous êtes connecté","Login");
                    logoutButton.setEnabled(true);
                    actualiserButton.setEnabled(true);
                    loginButton.setEnabled(false);
                    paymentButton.setEnabled(true);
                    getArticleButton.setEnabled(true);
                } else {
                    System.out.println("[loginButton] Login Invalide");
                    createDialoge("Erreur login","Login");
                }
            }
        });



        // Id, carte et nom de carte
        JPanel idPanel = new JPanel();
        idPanel.setLayout(new FlowLayout());
        JLabel idLabel = new JLabel("Id: ");
        idPanel.add(idLabel);
        idField = new JTextField(10);
        idPanel.add(idField);
        actualiserButton = new JButton("Actualiser");
        actualiserButton.setEnabled(false);
        idPanel.add(actualiserButton);
        getArticleButton = new JButton("Show articles");
        getArticleButton.setEnabled(false);
        idPanel.add(getArticleButton);
        add(idPanel);

        getArticleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("[getArticlesButton] press");
                if(dataGridChange && table.getSelectedRow() != -1){
                    int selectedRow = table.getSelectedRow();
                    int idArticle = Integer.parseInt((String) table.getValueAt(selectedRow, 0));
                    if(dataGridChange)
                        dataGridChange = false;

                    changeDataGrid(false);
                    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                    tableModel.setRowCount(0);

                    if (idArticle != -1) {

                        try
                        {
                            ArticleRequete requete = new ArticleRequete(idArticle);
                            oos.writeObject(requete);
                            ArticleResponse reponse = (ArticleResponse) ois.readObject();
                            if (reponse.getTaille() != 0) {
                                System.out.println("[ArticlesBouton] Il y a " + reponse.getTaille()+ " articles");
                                for(int i = 0; i < reponse.getTaille();i++){
                                    Article art = reponse.Articles.get(i);
                                    ajouterArticleDataGrid(art.getName(),art.getQuantity(),art.getPrice(),art.getTotalPrice());
                                }
                            } else {
                                createDialoge("Erreur recuperation article","Article");
                            }
                        }
                        catch (IOException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                    else {
                        createDialoge("Veuillez selectionner une facture","Articles");
                    }
                }
                else
                    createDialoge("Veuillez selectioner une facture","Article");
            }
        });




        // Datagrid pour afficher des données comprenant (IdFacture,date,montant)
        JPanel tablePanel = new JPanel(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("IdFacture");
        tableModel.addColumn("IdClient");
        tableModel.addColumn("Date");
        tableModel.addColumn("Montant");

        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 90));
        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel);

        // Possibilité de selectionner un element du datagrid
        ListSelectionModel listSelectionModel = table.getSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Bouton payement
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new FlowLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        paymentButton = new JButton("Payer");
        JLabel cardNumberLabel = new JLabel("Carte: ");
        paymentPanel.add(cardNumberLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cardNumberField = new JTextField(20);
        paymentPanel.add(cardNumberField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        JLabel nameLabel = new JLabel("Nom carte: ");
        paymentPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(20);
        paymentPanel.add(nameField, gbc);
        paymentPanel.add(paymentButton);
        paymentButton.setEnabled(false);
        add(paymentPanel);

        setSize(670, 450);
        setVisible(true);

        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ajoute le code pour effectuer le paiement ici
                System.out.println("[PayementButton] press");
                String tmp;
                int idFacture = -1;
                int idClient = Integer.parseInt(idField.getText());
                if(table.getSelectedRow() != -1 && !cardNumberField.getText().isEmpty() && !nameField.getText().isEmpty())
                {
                    try{

                        String Card = cardNumberField.getText().replace(" ","");
                        if(isNumeric(Card))
                        {
                            if(Card.length() >= 8)
                            {
                                int selectedRow = table.getSelectedRow();
                                if (selectedRow != -1) {
                                    tmp = (String) table.getValueAt(selectedRow, 0);
                                    idFacture = Integer.parseInt(tmp);
                                }

                                PayeRequete requete = new PayeRequete(Card,nameField.getText(),idFacture);
                                oos.writeObject(requete);
                                PayeResponse reponse = (PayeResponse) ois.readObject();
                                if(reponse.IsCardValide())
                                {
                                    createDialoge("Paiement effectué !","Paiement");
                                    cardNumberField.setText("");
                                    nameField.setText("");

                                    FactureRequete req = new FactureRequete(idClient);
                                    oos.writeObject(req);
                                    FactureResponse rep = (FactureResponse) ois.readObject();
                                    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                                    tableModel.setRowCount(0);
                                    if (rep.getTaille() != 0) {
                                        for(int i = 0; i < rep.getTaille();i++){
                                            Facture fac = rep.Factures.get(i);
                                            ajouterFactureDataGrid(fac.getIdFacture(),fac.getId(),fac.getDate(),fac.getMontant());
                                        }
                                    } else {
                                        System.out.println("[PayementButton] Update Data Gri: Plus de facture a paye");
                                        createDialoge("Il n'y a plus de facture a payer","Facture");
                                    }
                                }
                                else
                                    createDialoge("Carte invalide !","Carte");
                            }
                            else{
                                createDialoge("Carte doit comprendre 8 numero !","Carte");
                            }
                        }
                        else {
                            createDialoge("Le numero de carte ne peut contenir que des numéro !","Carte bancaire");
                        }

                    }
                    catch (Exception ex){
                        throw new RuntimeException(ex);
                    }
                }
                else{
                    if(table.getSelectedRow() == -1)
                        createDialoge("Veuillez selectionner une facture","Payer");
                    if(cardNumberField.getText().isEmpty())
                        createDialoge("Veuillez introduire une carte bancaire","Payer");
                    if(nameField.getText().isEmpty())
                        createDialoge("Veuillez introduire votre nom","Payer");
                }
            }
        });
        actualiserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("[ActualiserButton] press");
                DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                tableModel.setRowCount(0);
                if(!dataGridChange)
                {
                    changeDataGrid(true);
                    dataGridChange = true;
                }

                if(!idField.getText().isEmpty() && isNumeric(idField.getText()))
                {
                    int id = Integer.parseInt(idField.getText());
                    if(id > 0){
                        if(securePort)
                        {
                            System.out.println("GetFactureSecure");
                            GetFactureSecure(id);
                        }
                        else {
                            GetFactureUnsecure(id);
                        }
                    }
                    else
                        createDialoge("Id client ne peut etre plus petit que 1","ID");

                }
                else
                    createDialoge("Veuillez introduire un numero de client valide","ID");


            }
        });
    }
    public void GetFactureSecure(int id){
        try {
            System.out.println("Session key : "+sessionKey);
            FactureRequeteSecure requete = new FactureRequeteSecure(sessionKey,id);
            System.out.println(Arrays.toString(requete.getMsg()));
            oos.writeObject(requete);
            FactureResponseSecure reponse = (FactureResponseSecure) ois.readObject();
            byte[] msg = CryptData.DecryptSymDES(sessionKey,reponse.getMsg());
            String listFact = CryptData.ByteToString(msg);
            System.out.println("msg recu : "+listFact);
            String []SplitFact = listFact.split("\\$");
            System.out.println("Taille string getfacture reponse"+SplitFact.length);
            if(SplitFact[0].equalsIgnoreCase("nothing"))
            {
                createDialoge("Aucune facture impayé","Facture");
            }
            else
            {
                for(String fac : SplitFact){
                    System.out.println("String for boucle : "+ fac);
                    String [] OneFacture = fac.split(";");
                    ajouterFactureDataGrid(OneFacture[0],OneFacture[1],Date.valueOf(OneFacture[2]),Double.parseDouble(OneFacture[3]));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void GetFactureUnsecure(int id){
        try {
            FactureRequete requete = new FactureRequete(id);
            oos.writeObject(requete);
            FactureResponse reponse = (FactureResponse) ois.readObject();
            if (reponse.getTaille() != 0) {
                System.out.println("[ActualiserButton] Il y a " + reponse.getTaille()+ " Facture impayé");
                for(int i = 0; i < reponse.getTaille();i++){
                    Facture fac = reponse.Factures.get(i);
                    ajouterFactureDataGrid(fac.getIdFacture(),fac.getId(),fac.getDate(),fac.getMontant());
                }
            } else {
                createDialoge("Aucune facture impayé","Facture");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws IOException {
        ClientPaiementUI clientPaiementUI = new ClientPaiementUI();
    }
    public void ajouterFactureDataGrid(String id,String idClient, Date date, double montant)
    {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        Object[] nouvelleLigne = {id,idClient, date, montant};
        tableModel.addRow(nouvelleLigne);

    }
    public void ajouterArticleDataGrid(String name,int stock, Double price, double total)
    {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        Object[] nouvelleLigne = {name,stock, price, total};
        tableModel.addRow(nouvelleLigne);

    }
    public void changeDataGrid(boolean choice){
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setColumnCount(0);

        if(choice){

            System.out.println("Change data grid Factures");
            tableModel.addColumn("IdFacture");
            tableModel.addColumn("IdClient");
            tableModel.addColumn("Date");
            tableModel.addColumn("Montant");

            // Met à jour l'affichage
            table.updateUI();
        }
        else {
            System.out.println("Change data grid Articles");
            tableModel.addColumn("Articles");
            tableModel.addColumn("Quantité");
            tableModel.addColumn("Prix");
            tableModel.addColumn("Prix Total");

            // Met à jour l'affichage
            table.updateUI();
        }
    }
    public void createDialoge(String message,String title)
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }
    public boolean LoginUnsecure(){
        System.out.println("[loginButton] press");
        try{
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);
            LoginRequete requete = new LoginRequete(usernameField.getText(),password);
            oos.writeObject(requete);
            LoginResponse reponse = (LoginResponse) ois.readObject();
            if(reponse.isValide())
                return true;
            else
                return false;
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    public boolean LoginSecure(){
        System.out.println("[loginButton] press (Secure)");
        try{
            InitializeSessionSecure init = new InitializeSessionSecure(sessionKey);
            oos.writeObject(init);
            InitializeSessionResponse end = (InitializeSessionResponse) ois.readObject();
            if(end.isValide())
                System.out.println("Handshake Successfull");
            else
                System.out.println("Error Handshake session key");
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);
            System.out.println("Session key login: "+sessionKey);
            LoginRequeteSecure requete = new LoginRequeteSecure(usernameField.getText(),password,sessionKey);
            oos.writeObject(requete);
            LoginResponseSecure reponse = (LoginResponseSecure) ois.readObject();
            if(reponse.isValide()) {
                //sessionKey = reponse.getSessionKey();
                System.out.println("Clé de session récupérer !");
                return true;
            }
            else
                return false;
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
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
        SecretKey sessionKey = cleGen.generateKey();
        return sessionKey;
    }
    private void dialogSecureChoice(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Choice version", true);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Check the box for secure version");

        JCheckBox checkBox = new JCheckBox("Use secure version");

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isChecked = checkBox.isSelected();
                // Use isChecked value as needed
                if(checkBox.isSelected())
                {
                    System.out.println("Checkbox is checked: " + isChecked);
                    securePort = true;
                }
                else
                {
                    System.out.println("Checkbox is not checked: " + isChecked);
                    securePort = false;
                }
                dialog.dispose(); // Close the dialog
            }
        });

        panel.add(label);
        panel.add(checkBox);
        panel.add(okButton);

        dialog.add(panel);
        dialog.setSize(250, 150);
        dialog.setVisible(true);
    }
    private boolean isNumeric(String str) {
        return str.matches("\\d*");
    }

}
