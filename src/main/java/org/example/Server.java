package org.example;

import domain.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private int socketPortNumber;
    private ServerSocket serverSocket;

    private InetAddress address;

    public Server(int port) throws IOException {
        this.socketPortNumber = port;
        this.serverSocket = new ServerSocket(this.socketPortNumber);
        this.address = InetAddress.getLocalHost();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = this.serverSocket.accept();
                Client client = new Client(socket);
                client.start();
                System.out.println("Ciente conectado!\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } // while true
    }
}
