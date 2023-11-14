package Serveur.Protocole;

import java.util.ArrayList;

public class ArticleResponse implements  Reponse{


    public ArrayList<Article> Articles;
    private int taille;

    public ArticleResponse()
    {

        Articles = new ArrayList<Article>();
        taille = -1;

    }

    public int getTaille(){
        return Articles.size();
    }
}
