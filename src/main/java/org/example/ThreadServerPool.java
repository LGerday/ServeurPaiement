package org.example;

import java.io.IOException;

public class ThreadServerPool extends ThreadServer {
    private FileAttente connexionEnAttente;
    private ThreadGroup pool;
    private int taillePool;

    public ThreadServerPool(int port, int taillePool, Logger logger)throws IOException{
        super(port,logger);
        connexionEnAttente = new FileAttente();
        pool = new ThreadGroup("POOL");
        this.taillePool = taillePool;
    }

    @Override
    public void run(){

        try{
            for(int i = 0; i < taillePool; i++)
                new T
        }

    }
}
