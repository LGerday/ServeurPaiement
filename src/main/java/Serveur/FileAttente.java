package Serveur;

import java.net.Socket;
import java.util.LinkedList;

public class FileAttente {

    private LinkedList<Socket> fileAttente;

    public FileAttente()
    {
        fileAttente = new LinkedList<>();
    }

    public synchronized void addConnexion(Socket socket){
        fileAttente.add(socket);
        notify();
    }
    public synchronized Socket getConnexion() throws InterruptedException
    {
        while (fileAttente.isEmpty()) wait();
        return fileAttente.remove();
    }

}

