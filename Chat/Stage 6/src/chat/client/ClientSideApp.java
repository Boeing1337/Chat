package chat.client;

import chat.connection.InputReader;
import chat.connection.OutputWriter;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientSideApp {
    private final Scanner scanner = new Scanner(System.in);
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private Socket socket;

    private ClientSideApp() {
        createSocket();
        this.inputReader = new InputReader(socket);
        this.outputWriter = new OutputWriter(socket);
    }

    public static void main(final String[] args) {
        new ClientSideApp().start();
    }

    private void start() {
        announce();
        createInputThread();
        sending();
    }

    private void announce() {
        System.out.println("Client started!");
    }

    private void createInputThread() {
        new Thread(() ->
        {
            while (!socket.isClosed()) {
                System.out.println(inputReader.read());
            }
        }).start();
    }

    private void sending() {
        while (scanner.hasNextLine()) {
            final String input = scanner.nextLine().trim();
            outputWriter.sentMessage(input);
            if (input.equals("/exit")) {
                closeSocket();
                return;
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
                Thread.sleep(50);
            } catch (Exception ignored) {
                System.out.println("[CLIENT] Client is awoken from being asleep!");
            }
        }

    }
}
