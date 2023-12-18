package Serveur;

import Serveur.Protocole.Logger;
import Serveur.Protocole.Protocole;
import Serveur.Protocole.Secure.VESPAPS;
import Serveur.Protocole.UnSecure.VESPAP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class ServeurUI extends JFrame implements Logger {


    ThreadServer Server;
    private JButton startButton;
    private JButton stopButton;
    private JTable logTable;
    private DefaultTableModel logTableModel;
    private JButton clearLogButton;
    private JCheckBox secureBox;
    private JTextField threadNumberField;
    private JRadioButton poolBox,demandeBox;

    public ServeurUI() {
        setTitle("ServeurUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Panneau principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Boutons Démarrer et Arrêter le serveur
        ButtonGroup buttonGroup = new ButtonGroup();
        JPanel choicePanel = new JPanel();
        secureBox = new JCheckBox("Version sécurisé");
        poolBox = new JRadioButton("Serveur en pool");
        demandeBox = new JRadioButton("Serveur a la demande");
        threadNumberField = new JTextField(5);
        buttonGroup.add(poolBox);
        buttonGroup.add(demandeBox);

        choicePanel.add(secureBox);
        choicePanel.add(demandeBox);
        choicePanel.add(poolBox);
        choicePanel.add(threadNumberField);



        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Démarrer Serveur");
        stopButton = new JButton("Arrêter Serveur");
        buttonPanel.add(choicePanel);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        stopButton.setEnabled(false);

        // Datagrid pour afficher des logs (Thread et Action)
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BorderLayout());
        logTableModel = new DefaultTableModel(new Object[]{"Thread", "Action"}, 0);
        logTable = new JTable(logTableModel);
        logPanel.add(new JScrollPane(logTable), BorderLayout.CENTER);

        // Bouton pour vider les logs
        clearLogButton = new JButton("Vider les Logs");
        logPanel.add(clearLogButton, BorderLayout.SOUTH);

        // Ajout des éléments au panneau principal
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(logPanel, BorderLayout.CENTER);

        // Ajoute un écouteur d'événements au bouton Démarrer
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("[Server] Starting");
                int port = 0;
                Protocole myProtocole = null;
                if(secureBox.isSelected())
                {
                   myProtocole = new VESPAPS(ServeurUI.this);
                }
                else
                {
                    // pas secure
                    myProtocole  = new VESPAP(ServeurUI.this);
                }
                
                try (BufferedReader lecteur = new BufferedReader(new FileReader("src/main/java/Serveur/properties.conf"))) {

                    System.out.println("[Server] Reading conf file");
                    String tmp = lecteur.readLine();
                    String[] tmp2 = tmp.split("=");
                    port = Integer.parseInt(tmp2[1]);
                    if(secureBox.isSelected())
                    {
                        tmp = lecteur.readLine();
                        tmp2 = tmp.split("=");
                        port = Integer.parseInt(tmp2[1]);
                    }
                    System.out.println("[Server] Port : "+ port);

                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
                if(demandeBox.isSelected()){
                    // créeer le serveur sur demande
                    try {
                        Server = new ThreadServerDemande(port,myProtocole,ServeurUI.this);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if(poolBox.isSelected()){
                    try {
                        Server = new ThreadServerPool(port,myProtocole,Integer.parseInt(threadNumberField.getText()),ServeurUI.this);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                Server.start();
                stopButton.setEnabled(true);
                startButton.setEnabled(false);
                System.out.println("[Server] Started");


            }
        });

        // Ajoute un écouteur d'événements au bouton Arrêter
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Insérez votre code d'arrêt du serveur ici
                // Vous pouvez ajouter des logs en utilisant logTableModel.addRow(new Object[]{"ThreadName", "Arrêter le serveur"});
                Server.interrupt();
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                System.out.println("[Server] Stopping");
            }
        });

        // Ajoute un écouteur d'événements au bouton Vider les Logs
        clearLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Efface les logs en supprimant toutes les lignes du modèle de données
                System.out.println("[Server] Clearing logs");
                while (logTableModel.getRowCount() > 0) {
                    logTableModel.removeRow(0);
                }
            }
        });

        add(mainPanel);
        setVisible(true);


    }
    @Override
    public void Trace(String message)
    {
        DefaultTableModel modele = (DefaultTableModel) logTable.getModel();
        Vector<String> ligne = new Vector<>();
        ligne.add(Thread.currentThread().getName());
        ligne.add(message);
        modele.insertRow(0,ligne);
    }
    private void videLogs()
    {
        DefaultTableModel modele = (DefaultTableModel) logTable.getModel();
        modele.setRowCount(0);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServeurUI();
            }
        });
    }

}
