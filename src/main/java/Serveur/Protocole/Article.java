package Serveur.Protocole;

import java.io.Serializable;

public class Article implements Serializable {

    private int quantity;

    private String name;

    private Double price;

    private Double totalPrice;


    public Article(int quantity,String name,Double price,Double totalPrice){
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity(){return quantity;}
    public String getName(){return name;}
    public Double getPrice(){return price;}
    public Double getTotalPrice(){return totalPrice;}
}
