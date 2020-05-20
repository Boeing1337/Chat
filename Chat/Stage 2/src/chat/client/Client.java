package chat.client;

import chat.util.InputReader;
import chat.util.OutputWriter;

import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private Socket clientSocket;

    public static void main(final String[] args) {
        new Client().start();
    }

    private void start() {
        announce();
        createSocket();
        createIOWorkers();
        pause();
        closeSocket();
    }

    private void announce() {
        System.out.println("Client started!");
    }

    private void pause() {
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {
        }
    }

    private void closeSocket() {
        try {
            clientSocket.close();
        } catch (Exception ignored) {
        }
    }


    private void createIOWorkers() {
        createInputReader(clientSocket);
        createOutputWriter(clientSocket);
    }

    private void createInputReader(final Socket clientSocket) {
        new Thread(new InputReader(clientSocket)).start();
    }

    private void createOutputWriter(final Socket clientSocket) {
        new Thread(new OutputWriter(clientSocket)).start();
    }

    private void createSocket() {
        final String address = "127.0.0.1";
        final int port = 23456;
        while (true) {
            try {
                clientSocket = new Socket(InetAddress.getByName(address), port);
                return;
            } catch (Exception e) {
                System.out.println("\n" + e + "\n[CLIENT] Can't connect to the server");
            }
            try {
                Thread.sleep(50);
            } catch (Exception ignored) {
                System.out.println("[CLIENT] Client is awoken from being asleep!");
            }
        }
    }
}
