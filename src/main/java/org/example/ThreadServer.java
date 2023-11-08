package org.example;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class ThreadServer extends Thread
{
    protected int port;
    protected Protocole protocole;
    protected Logger logger;
    protected ServerSocket ssocket;
    public ThreadServer(int port, Protocole protocole, Logger logger) throws IOException
    {
        //super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");
        super("TH Serveur (port=" + port + ",protocole= ");
        this.port = port;
        this.protocole = protocole;
        this.logger = logger;
        ssocket = new ServerSocket(port);
    }
}
