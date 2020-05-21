package chat.client;

import chat.connection.InputReader;
import chat.connection.OutputWriter;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Scanner scanner = new Scanner(System.in);
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private Socket socket;

    private Client() {
        createSocket();
        this.inputReader = new InputReader(socket);
        this.outputWriter = new OutputWriter(socket);
    }

    public static void main(final String[] args) {
        System.out.println("Client started!");
        new Client().start();
    }

    private void start() {
        registration();
        createInputThread();
        chatting();
    }

    private void createInputThread() {
        new Thread(() ->
        {
            while (!socket.isClosed()) {
                System.out.println(inputReader.read());
            }
        }).start();
    }

    private void chatting() {
        while (scanner.hasNextLine()) {
            final String input = scanner.nextLine().trim();
            outputWriter.sentMessage(input);
            if (input.equals("/exit")) {
                closeSocket();
                return;
            }

        }
    }

    private void registration() {
        System.out.println(inputReader.read());
        while (scanner.hasNextLine()) {
            final String input = scanner.nextLine().trim();
            outputWriter.sentMessage(input);

            final String answer = inputReader.read().trim();
            if (answer.equals("welcome"))
                break;
            System.out.println(answer);
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
