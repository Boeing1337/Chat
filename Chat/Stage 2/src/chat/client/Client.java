package chat.client;

import chat.util.InputReader;
import chat.util.MessageContainer;
import chat.util.OutputWriter;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private MessageContainer inputMessage = new MessageContainer();
    private MessageContainer outputMessage = new MessageContainer();
    private Socket clientSocket;

    public static void main(final String[] args) {
        new Client().start();
    }

    private void start() {
        createSocket();
        createIOWorkers();
        scannerWork();


    }

    private void scannerWork() {
        final Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            final String temp = scanner.nextLine().trim();
            if (temp.equals("/show")) {
                System.out.println(inputMessage.getMessage());

                return;
            } else {
                outputMessage.setMessage(temp);
            }
        }
    }


    private void createIOWorkers() {
        createInputReader(clientSocket);
        createOutputWriter(clientSocket);

    }

    private void createInputReader(final Socket clientSocket) {
        new Thread(new InputReader(clientSocket, inputMessage)).start();
    }

    private void createOutputWriter(final Socket clientSocket) {
        new Thread(new OutputWriter(clientSocket, outputMessage)).start();
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
                Thread.sleep(500);
            } catch (Exception ignored) {
                System.out.println("[CLIENT] Client is awoken from being asleep!");
            }
        }

    }
}
