package JDBC;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BeanJDBC implements Runnable
{
    private Connection con = null;
    private String query;
    private ResultSet rs;
    private int RU;

    // Pour la connection
    public BeanJDBC(int type)
    {
        if(type==1)
        {
            try
            {
                System.out.println("Essai de connexion JDBC");
                Class leDriver = Class.forName("oracle.jdbc.driver.OracleDriver");

                con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/orcl", "rti", "oracle");
                System.out.println("Connexion à la BDD sys réalisée");
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        else if(type==2)
        {
            try
            {
                System.out.println("Essai de connexion JDBC");
                Class leDriver = Class.forName("com.mysql.cj.jdbc.Driver");

                // reprenons les choses sérieuses

                con = DriverManager.getConnection("jdbc:mysql://localhost:50000/db_holidays","root","oracle");
                System.out.println("Connexion à la BDD sys réalisée");
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void run()
    {
        try {
            execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(BeanJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void execute(String query) throws SQLException
    {
        if(query.contains("select"))
        {
            try
            {
                Statement instruc = con.createStatement();
                rs = instruc.executeQuery(query);// requête classique sql
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        else
        {
            Statement instruc = con.createStatement();
            RU = instruc.executeUpdate(query); //update de la base de données
        }


    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return the rs
     */
    public ResultSet getRs() {
        return rs;
    }

}
