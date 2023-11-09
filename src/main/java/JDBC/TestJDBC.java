package JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestJDBC {
    public static void main(String[] args) {
        // Crée une instance de BeanJDBC
        BeanJDBC beanJDBC = new BeanJDBC();

        // Définis la requête SELECT
        String query = "select * from employes";

        // Appelle la méthode setQuery pour définir la requête
        beanJDBC.setQuery(query);

        // Utilise la méthode execute pour exécuter la requête
        try {
            beanJDBC.execute(query);

            // Récupère le ResultSet et affiche les résultats
            ResultSet rs = beanJDBC.getRs();
            while (rs.next()) {
                // Récupère les colonnes par leur nom (ajuste les noms selon ta structure de base de données)
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                // Affiche les résultats
                System.out.println("ID: " + id + ", Username: " + username + ", Password: " + password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
