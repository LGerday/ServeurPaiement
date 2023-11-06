package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class ThreadClient extends Thread  {
    protected Socket csocket;

    protected Logger logger;
    private int numero;
    private static int numCourant = 1;

    public ThreadClient(Socket csocket,Logger logger)throws IOException{

        this.csocket = csocket;
        this.logger = logger;
        this.numero = numCourant++;

    }

    @Override
    public void run(){

        try {
            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;
            try {
                ois = new ObjectInputStream(csocket.getInputStream());
                oos = new ObjectOutputStream(csocket.getOutputStream());

                while(true){

                }
            }
            catch (Exception ex)
            {
                logger.Trace("Fin de connexion demandée");
            }
        }
        catch (IOException ex){
            logger.Trace("Erreur I/O");
        }

    }

}
