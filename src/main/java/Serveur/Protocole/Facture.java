package Serveur.Protocole;

import java.io.Serializable;
import java.sql.Date;

public class Facture implements Serializable {

    private int idFacture;
    private int id;

    private Date date;

    private Double montant;


    public Facture(int idFacture,int id,Date date,Double montant){
        this.idFacture = idFacture;
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
    public String getIdFacture(){return String.valueOf(idFacture);};
}
