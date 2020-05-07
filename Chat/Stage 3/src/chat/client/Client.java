package chat.client;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private OutputWriter outputWriter;
    private InputReader inputReader;
    private Socket clientSocket;

    public static void main(final String[] args) {
        new Client().start();
    }

    private void start() {
        createSocket();
        startIOWorkers();
        scannerWork();


    }

    private void scannerWork() {
        final Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            final String temp = scanner.nextLine().trim();
            outputWriter.addOutputMessage(temp);
            if (temp.equals("/exit")) {
                printInputMessages();
                closeSocket();
                return;
            }


        }
    }

    private void closeSocket() {
        try {
            Thread.sleep(500);
            clientSocket.close();
        } catch (Exception ignored) {

        }
    }

    private void printInputMessages() {
        for (String s : inputReader.getAllMessages()) {
            System.out.println(s);
        }
    }


    private void startIOWorkers() {
        startInputReader();
        startOutputWriter();

    }

    private void startInputReader() {
        inputReader = new InputReader(clientSocket);
        new Thread(inputReader).start();
    }

    private void startOutputWriter() {
        outputWriter = new OutputWriter(clientSocket);
        new Thread(outputWriter).start();
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
