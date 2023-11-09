package Serveur.Protocole;

import java.io.Serializable;
import java.sql.Date;

public class Facture implements Serializable {

    private int id;

    private Date date;

    private Double montant;


    public Facture(int id,Date date,Double montant){
        this.id = id;
        this.date = date;
        this.montant = montant;
    }

    public String getId(){
        return String.valueOf(id);
    }
    public Date getDate(){
        return date;
    }
    public Double getMontant(){
        return montant;
    }
}
