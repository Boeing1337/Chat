package chat.client;

import chat.connection.InputReader;
import chat.connection.OutputWriter;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private Socket socket;

    private Client() {
        createSocket();
        this.inputReader = new InputReader(socket);
        this.outputWriter = new OutputWriter(socket);
        System.out.println("Client started!");
    }

    public static void main(final String[] args) {
        new Client().start();
    }

    private void start() {
        final Scanner scanner = new Scanner(System.in);
        while (!socket.isClosed() && scanner.hasNext()) {

            final String message = scanner.nextLine().trim();
            outputWriter.sentMessage(message);

            if ("/exit".equals(message)) {
                closeSocket();
                return;
            } else {
                System.out.println(inputReader.read());
            }
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (Exception ignored) {
        }
    }

    private void createSocket() {
        final String address = "127.0.0.1";
        final int port = 23456;
        while (true) {
            try {
                socket = new Socket(InetAddress.getByName(address), port);
                return;
            } catch (Exception e) {
                System.out.println("\n" + e + "\n[CLIENT] Can't connect to the server");
            }
            try {
                Thread.sleep(500);
            } catch (Exception ignored) {
                System.out.println("[CLIENT] Client is awoken from being asleep!");
            }
        }

    }
}
