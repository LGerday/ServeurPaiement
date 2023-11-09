package JDBC;

import java.beans.Beans;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class APP_TEST_LIB_JDBC extends javax.swing.JFrame {

    BeanJDBC bean;

    public APP_TEST_LIB_JDBC() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButtonConnecter = new javax.swing.JButton();
        jRadioButtonOracle = new javax.swing.JRadioButton();
        jRadioButtonMySQL = new javax.swing.JRadioButton();

        jFrame1.setMinimumSize(new java.awt.Dimension(730, 426));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Voyageurs", "Reservations", "Chambres", "Activites" }));

        jButton1.setText("Afficher");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AfficherBoutonActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton2.setText("Executer");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExecuterBoutonActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
                jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGap(297, 297, 297)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame1Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                                .addGap(64, 64, 64))
                        .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGap(122, 122, 122)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(122, 122, 122))
        );
        jFrame1Layout.setVerticalGroup(
                jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(66, 66, 66)
                                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButtonConnecter.setText("Se connecter");
        jButtonConnecter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConnecterActionPerformed(evt);
            }
        });

        jRadioButtonOracle.setText("Oracle");
        jRadioButtonOracle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonOracleActionPerformed(evt);
            }
        });

        jRadioButtonMySQL.setText("MySQL");
        jRadioButtonMySQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMySQLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(102, Short.MAX_VALUE)
                                .addComponent(jRadioButtonMySQL)
                                .addGap(89, 89, 89)
                                .addComponent(jRadioButtonOracle)
                                .addGap(91, 91, 91))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(150, 150, 150)
                                .addComponent(jButtonConnecter)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jRadioButtonOracle)
                                        .addComponent(jRadioButtonMySQL))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                                .addComponent(jButtonConnecter)
                                .addGap(81, 81, 81))
        );

        pack();
    }// </editor-fold>

    private void jButtonConnecterActionPerformed(java.awt.event.ActionEvent evt) {
        if(jRadioButtonOracle.isSelected()) bean = new BeanJDBC(1); //connexion oracle, envoie type
        else if (jRadioButtonMySQL.isSelected()) bean = new BeanJDBC(2); //connexion Mysql, envoie type
        this.setVisible(false);
        jFrame1.setVisible(true);

    }

    private void AfficherBoutonActionPerformed(java.awt.event.ActionEvent evt) {

        int index = jComboBox1.getSelectedIndex();

        if(index == 0)
        {
            try {
                bean.setQuery("Select * from Voyageurs"); // On lui donne la requête au préalable
                Thread thread = new Thread(bean); // On crée un thread par dessus
                thread.start(); // On lance le thread qui lance la méthode run de bean
                thread.join(); // On attend la fin du thread pour avoir des résultats aussi non c'est trop rapide rs=null
                ResultSet rs = bean.getRs(); // On récupère les résultats
                jTextArea1.setText(null);
                while(rs.next())
                {
                    String Voy = rs.getString("Voyageur"); //colonne
                    jTextArea1.append(Voy);

                    String Voy1 = rs.getString("Voyageur1");
                    jTextArea1.append(" ");
                    jTextArea1.append(Voy1);

                    int NumCli = rs.getInt("NumClient");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(NumCli));

                    String Nom = rs.getString("Nom");
                    jTextArea1.append(" ");
                    jTextArea1.append(Nom);

                    String Prenom = rs.getString("Prenom");
                    jTextArea1.append(" ");
                    jTextArea1.append(Prenom);

                    String Groupe = rs.getString("Groupe");
                    jTextArea1.append(" ");
                    jTextArea1.append(Groupe);

                    String Nationalite = rs.getString("Nationalite");
                    jTextArea1.append(" ");
                    jTextArea1.append(Nationalite);

                    Date DateNaissance = rs.getDate("DateNaissance");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(DateNaissance));

                    String Email = rs.getString("Email");
                    jTextArea1.append(" ");
                    jTextArea1.append(Email);

                    jTextArea1.append(" \n");

                }
            } catch (SQLException ex) {
                Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(index == 1)
        {
            try {
                bean.setQuery("Select * from Reservations"); // On lui donne la requête au préalable
                Thread thread = new Thread(bean); // On crée un thread par dessus
                thread.start(); // On lance le thread qui lance la méthode run de bean
                thread.join(); // On attend la fin du thread pour avoir des résultats aussi non c'est trop rapide rs=null
                ResultSet rs = bean.getRs(); // On récupère les résultats
                jTextArea1.setText(null);
                while(rs.next())
                {
                    int Id = rs.getInt("Id");
                    jTextArea1.append(String.valueOf(Id));

                    String VoyageurTitu = rs.getString("VoyageurTitu");
                    jTextArea1.append(" ");
                    jTextArea1.append(VoyageurTitu);

                    int Reference = rs.getInt("Reference");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(Reference));

                    int PrixNet = rs.getInt("PrixNet");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(PrixNet));

                    Date DateDebut = rs.getDate("DateDebut");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(DateDebut));

                    Date DateFin = rs.getDate("DateFin");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(DateFin));

                    String Paye = rs.getString("Paye");
                    jTextArea1.append(" ");
                    jTextArea1.append(Paye);


                    jTextArea1.append(" \n");

                }
            } catch (SQLException ex) {
                Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(index == 2)
        {
            try {
                bean.setQuery("Select * from Chambres"); // On lui donne la requête au préalable
                Thread thread = new Thread(bean); // On crée un thread par dessus
                thread.start(); // On lance le thread qui lance la méthode run de bean
                thread.join(); // On attend la fin du thread pour avoir des résultats aussi non c'est trop rapide rs=null
                ResultSet rs = bean.getRs(); // On récupère les résultats
                jTextArea1.setText(null);
                while(rs.next())
                {
                    int Numero = rs.getInt("Numero");
                    jTextArea1.append(String.valueOf(Numero));

                    String Equipement = rs.getString("Equipement");
                    jTextArea1.append(" ");
                    jTextArea1.append(Equipement);

                    int NbrOccupant = rs.getInt("NbrOccupant");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(NbrOccupant));

                    int PrixHTVA = rs.getInt("PrixHTVA");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(PrixHTVA));


                    jTextArea1.append(" \n");

                }
            } catch (SQLException ex) {
                Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(index == 3)
        {
            try {
                bean.setQuery("Select * from Activites"); // On lui donne la requête au préalable
                Thread thread = new Thread(bean); // On crée un thread par dessus
                thread.start(); // On lance le thread qui lance la méthode run de bean
                thread.join(); // On attend la fin du thread pour avoir des résultats aussi non c'est trop rapide rs=null
                ResultSet rs = bean.getRs(); // On récupère les résultats
                jTextArea1.setText(null);
                while(rs.next())
                {
                    int Identifiant = rs.getInt("Identifiant");
                    jTextArea1.append(String.valueOf(Identifiant));

                    int Type  = rs.getInt("Type");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(Type));

                    int NbrMax = rs.getInt("NbrMax");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(NbrMax));

                    int NbrInscrit = rs.getInt("NbrInscrit");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(NbrInscrit));

                    int Duree = rs.getInt("Duree");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(Duree));

                    int PriceHTVA = rs.getInt("PriceHTVA");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(PriceHTVA));

                    jTextArea1.append(" \n");

                }
            } catch (SQLException ex) {
                Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //executer la requete mise par l'utilisateur
    private void ExecuterBoutonActionPerformed(java.awt.event.ActionEvent evt) {

        bean.setQuery(jTextField1.getText()); // On lui donne la requête au préalable
        Thread thread = new Thread(bean); // On crée un thread par dessus
        thread.start(); // On lance le thread qui lance la méthode run de bean
        try {
            thread.join(); // On attend la fin du thread pour avoir des résultats aussi non c'est trop rapide rs=null
        } catch (InterruptedException ex) {
            Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResultSet rs = bean.getRs(); // On récupère les résultats
        jTextArea1.setText(null);

        if(jTextField1.getText().equals("select * from Voyageurs"))
        {

            try {
                while(rs.next())
                {
                    String Voy = rs.getString("Voyageur");
                    jTextArea1.append(Voy);

                    String Voy1 = rs.getString("Voyageur1");
                    jTextArea1.append(" ");
                    jTextArea1.append(Voy1);

                    int NumCli = rs.getInt("NumClient");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(NumCli));

                    String Nom = rs.getString("Nom");
                    jTextArea1.append(" ");
                    jTextArea1.append(Nom);

                    String Prenom = rs.getString("Prenom");
                    jTextArea1.append(" ");
                    jTextArea1.append(Prenom);

                    String Groupe = rs.getString("Groupe");
                    jTextArea1.append(" ");
                    jTextArea1.append(Groupe);

                    String Nationalite = rs.getString("Nationalite");
                    jTextArea1.append(" ");
                    jTextArea1.append(Nationalite);

                    Date DateNaissance = rs.getDate("DateNaissance");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(DateNaissance));

                    String Email = rs.getString("Email");
                    jTextArea1.append(" ");
                    jTextArea1.append(Email);

                    jTextArea1.append(" \n");

                }
            } catch (SQLException ex) {
                Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(jTextField1.getText().equals("select * from Reservations"))
        {
            try {
                while(rs.next())
                {
                    int Id = rs.getInt("Id");
                    jTextArea1.append(String.valueOf(Id));

                    String VoyageurTitu = rs.getString("VoyageurTitu");
                    jTextArea1.append(" ");
                    jTextArea1.append(VoyageurTitu);

                    int Reference = rs.getInt("Reference");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(Reference));

                    int PrixNet = rs.getInt("PrixNet");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(PrixNet));

                    Date DateDebut = rs.getDate("DateDebut");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(DateDebut));

                    Date DateFin = rs.getDate("DateFin");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(DateFin));

                    String Paye = rs.getString("Paye");
                    jTextArea1.append(" ");
                    jTextArea1.append(Paye);


                    jTextArea1.append(" \n");

                }
            } catch (SQLException ex) {
                Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(jTextField1.getText().equals("select * from Chambres"))
        {
            try {
                while(rs.next())
                {
                    int Numero = rs.getInt("Numero");
                    jTextArea1.append(String.valueOf(Numero));

                    String Equipement = rs.getString("Equipement");
                    jTextArea1.append(" ");
                    jTextArea1.append(Equipement);

                    int NbrOccupant = rs.getInt("NbrOccupant");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(NbrOccupant));

                    int PrixHTVA = rs.getInt("PrixHTVA");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(PrixHTVA));


                    jTextArea1.append(" \n");

                }
            } catch (SQLException ex) {
                Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(jTextField1.getText().equals("select * from Activites"))
        {
            try {
                while(rs.next())
                {
                    int Identifiant = rs.getInt("Identifiant");
                    jTextArea1.append(String.valueOf(Identifiant));

                    int Type  = rs.getInt("Type");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(Type));

                    int NbrMax = rs.getInt("NbrMax");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(NbrMax));

                    int NbrInscrit = rs.getInt("NbrInscrit");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(NbrInscrit));

                    int Duree = rs.getInt("Duree");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(Duree));

                    int PriceHTVA = rs.getInt("PriceHTVA");
                    jTextArea1.append(" ");
                    jTextArea1.append(String.valueOf(PriceHTVA));

                    jTextArea1.append(" \n");

                }
            } catch (SQLException ex) {
                Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }

    private void jRadioButtonOracleActionPerformed(java.awt.event.ActionEvent evt) {

        if (jRadioButtonMySQL.isSelected()) jRadioButtonMySQL.setSelected(false);
    }

    private void jRadioButtonMySQLActionPerformed(java.awt.event.ActionEvent evt) {

        if (jRadioButtonOracle.isSelected()) jRadioButtonOracle.setSelected(false);
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(APP_TEST_LIB_JDBC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new APP_TEST_LIB_JDBC().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonConnecter;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JRadioButton jRadioButtonMySQL;
    private javax.swing.JRadioButton jRadioButtonOracle;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration
}