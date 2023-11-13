package Client;

import Serveur.Protocole.*;
import Serveur.ThreadServerPool;

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
import java.sql.Date;

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

    public ClientPaiementUI() throws IOException {
        super("ClientPaiementUI");
        socket = new Socket("192.168.0.105",50000);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Id, carte et nom de carte
        JPanel idPanel = new JPanel();
        idPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel idLabel = new JLabel("Id: ");
        idPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        idField = new JTextField(10);
        idPanel.add(idField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        JLabel cardNumberLabel = new JLabel("Carte: ");
        idPanel.add(cardNumberLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cardNumberField = new JTextField(20);
        idPanel.add(cardNumberField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        JLabel nameLabel = new JLabel("Nom carte: ");
        idPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(20);
        idPanel.add(nameField, gbc);
        add(idPanel);

        // Login avec username et password et un bouton
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
                System.out.println("Fermeture socket et flux");
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
                // Effectue les opérations de logout ici
                try {
                    LogoutRequete requete = new LogoutRequete(usernameField.getText());
                    oos.writeObject(requete);
                    LogoutResponse reponse = (LogoutResponse) ois.readObject();
                    System.out.println("Test Avant reponse");
                    if (reponse.getValide()) {
                        System.out.println("Logout Valide");
                        logoutButton.setEnabled(false);
                        loginButton.setEnabled(true);
                        paymentButton.setEnabled(false);
                        usernameField.setText("");
                        passwordField.setText("");
                    } else {
                        System.out.println("Logout pas valide");
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    char[] passwordChars = passwordField.getPassword();
                    String password = new String(passwordChars);
                    LoginRequete requete = new LoginRequete(usernameField.getText(),password);
                    oos.writeObject(requete);
                    LoginResponse reponse = (LoginResponse) ois.readObject();
                    System.out.println("Test Avant reponse");
                    if (reponse.isValide()) {
                        System.out.println("Login Valide");

                        createDialoge("Vous êtes connecté","Login");
                        logoutButton.setEnabled(true);
                        loginButton.setEnabled(false);
                        paymentButton.setEnabled(true);
                    } else {
                        System.out.println("Login pas valide");
                        createDialoge("Erreur login","Login");
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

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
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
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
        paymentButton = new JButton("Payer");
        actualiserButton = new JButton("Actualiser");
        paymentPanel.add(actualiserButton);
        paymentPanel.add(paymentButton);
        paymentButton.setEnabled(false);
        add(paymentPanel);

        setSize(650, 450);
        setVisible(true);

        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ajoute le code pour effectuer le paiement ici
                String tmp;
                int idFacture = -1;
                if(table.getSelectedRow() != -1 && !cardNumberField.getText().isEmpty() && !nameField.getText().isEmpty())
                {
                    try{

                        String Card = cardNumberField.getText();
                        if(isNumeric(Card))
                        {
                            if(Card.length() >= 8)
                            {
                                int selectedRow = table.getSelectedRow();
                                if (selectedRow != -1) {
                                    tmp = (String) table.getValueAt(selectedRow, 0);
                                    idFacture = Integer.parseInt(tmp);
                                    System.out.println(idFacture);
                                }
                                System.out.println(idFacture);
                                PayeRequete requete = new PayeRequete(cardNumberField.getText(),nameField.getText(),idFacture);
                                oos.writeObject(requete);
                                PayeResponse reponse = (PayeResponse) ois.readObject();
                                if(reponse.IsCardValide())
                                {
                                    createDialoge("Carte Valide !","Carte");
                                    cardNumberField.setText("");
                                    nameField.setText("");
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
                DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                tableModel.setRowCount(0);
                try {
                    if(!idField.getText().isEmpty() && isNumeric(idField.getText()))
                    {
                        int id = Integer.parseInt(idField.getText());

                        FactureRequete requete = new FactureRequete(id);
                        oos.writeObject(requete);
                        FactureResponse reponse = (FactureResponse) ois.readObject();
                        System.out.println("Test Avant reponse");
                        if (reponse.getTaille() != 0) {
                            System.out.println("Il y a " + reponse.getTaille()+ "Facture impayé");
                            for(int i = 0; i < reponse.getTaille();i++){
                                Facture fac = reponse.Factures.get(i);
                                ajouterFactureDataGrid(fac.getIdFacture(),fac.getId(),fac.getDate(),fac.getMontant());
                            }
                        } else {
                            System.out.println("Aucune facture impayé");
                            createDialoge("Aucune facture impayé","Facture");
                        }
                    }
                    else
                        createDialoge("Veuillez introduire un numero de client valide","ID");
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
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
    public void createDialoge(String message,String title)
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }
    private boolean isNumeric(String str) {
        return str.matches("\\d*");
    }
}
