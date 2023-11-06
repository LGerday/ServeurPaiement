package org.example;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class ThreadServer {
    protected int port;
    protected ServerSocket ssocket;
    protected Logger logger;

    public ThreadServer(int port, Logger logger) throws IOException {
        this.port = port;
        ssocket = new ServerSocket(port);
        this.logger = logger;
    }

}
