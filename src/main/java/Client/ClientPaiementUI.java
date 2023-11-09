package Client;

import Serveur.Protocole.*;
import Serveur.ThreadServerPool;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Effectue les opérations de logout ici
                // Par exemple, ferme la connexion, réinitialise les champs, etc.
                // Pour l'instant, je vais simplement désactiver le bouton "Logout"
                logoutButton.setEnabled(false);

                // Rend le bouton "Login" cliquable
                loginButton.setEnabled(true);

                // Rend le bouton "Payer" non cliquable
                paymentButton.setEnabled(false);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    char[] passwordChars = passwordField.getPassword();
                    String password = new String(passwordChars);
                    LoginRequete requete = new LoginRequete(usernameField.getText(),password);
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    ois = new ObjectInputStream(socket.getInputStream());
                    oos.writeObject(requete);
                    LoginResponse reponse = (LoginResponse) ois.readObject();
                    System.out.println("Test Avant reponse");
                    if (reponse.isValide()) {
                        System.out.println("Login Valide");
                        // Rend le bouton "Logout" cliquable
                        logoutButton.setEnabled(true);
                        // Rend le bouton "Login" non cliquable
                        loginButton.setEnabled(false);
                        // Rend le bouton "Payer" cliquable
                        paymentButton.setEnabled(true);
                    } else {
                        System.out.println("Login pas valide");
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
                System.out.println("Paiement effectué !");
            }
        });
        actualiserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String Card = cardNumberField.getText();
                    FactureRequete requete = new FactureRequete(id);
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    ois = new ObjectInputStream(socket.getInputStream());
                    oos.writeObject(requete);
                    FactureResponse reponse = (FactureResponse) ois.readObject();
                    System.out.println("Test Avant reponse");
                    if (reponse.getTaille() != 0) {
                        System.out.println("Il y a " + reponse.getTaille()+ "Facture impayé");
                        for(int i = 0; i < reponse.getTaille();i++){
                            Facture fac = reponse.Factures.get(i);
                            ajouterFactureDataGrid(fac.getId(),fac.getDate(),fac.getMontant());
                        }
                    } else {
                        System.out.println("Aucune facture impayé");
                        createDialoge("Aucune facture impayé","Facture");
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }

    public static void main(String[] args) throws IOException {
        ClientPaiementUI clientPaiementUI = new ClientPaiementUI();
    }
    public void ajouterFactureDataGrid(String id, Date date, double montant)
    {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        Object[] nouvelleLigne = {id, date, montant};
        tableModel.addRow(nouvelleLigne);

    }
    public void createDialoge(String message,String title)
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
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

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }
}
