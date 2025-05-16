package org.example;

import sockets.Server;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Server");
        try {
            new Server(5025).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}