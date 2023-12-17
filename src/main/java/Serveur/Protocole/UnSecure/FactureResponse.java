package Serveur.Protocole.UnSecure;

import Serveur.Protocole.Facture;
import Serveur.Protocole.Reponse;

import java.util.ArrayList;


public class FactureResponse implements Reponse {

        public ArrayList<Facture> Factures;
        private int taille;

        public FactureResponse()
        {

            Factures = new ArrayList<Facture>();
            taille = 0;

        }

        public int getTaille(){
            return Factures.size();
       }

}

