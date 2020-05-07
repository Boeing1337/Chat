package chat.server;

import chat.util.InputReader;
import chat.util.MessageContainer;
import chat.util.OutputWriter;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private MessageContainer inputMessage = new MessageContainer();
    private MessageContainer outputMessage = new MessageContainer();
    private ServerSocket ss;

    public static void main(final String[] args) {
        new Server().start();
    }

    private void start() {
        createServerSocket();
        createIOWorkers();
        scannerWork();
    }

    private void scannerWork() {
        final Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            final String temp = scanner.nextLine().trim();
            if (temp.equals("/show")) {
                System.out.println(inputMessage.getMessage());
                closeSocket();
                return;
            } else {
                outputMessage.setMessage(temp);
            }
        }
    }

    private void closeSocket() {
        try {
            ss.close();
        } catch (Exception ignored) {

        }
    }


    private void createIOWorkers() {
        new Thread(() -> {
            while (!ss.isClosed()) {
                final Socket clientSocket = getConnection();
                if (clientSocket == null)
                    continue;
                createInputReader(clientSocket);
                createOutputWriter(clientSocket);
            }
        }).start();
    }

    private Socket getConnection() {
        try {
            return ss.accept();
        } catch (Exception e) {
            System.out.println("[SERVER] Can't accept connection!");
        }
        return null;
    }

    private void createInputReader(final Socket clientSocket) {
        new Thread(new InputReader(clientSocket, inputMessage)).start();
    }

    private void createOutputWriter(final Socket clientSocket) {
        new Thread(new OutputWriter(clientSocket, outputMessage)).start();
    }

    private void createServerSocket() {
        String address = "127.0.0.1";
        int port = 23456;
        while (true) {
            try {
                ss = new ServerSocket(port, 50, InetAddress.getByName(address));
                return;
            } catch (Exception ignored) {
                System.out.println("[SERVER] Can't create a socket!");
            }
            try {
                Thread.sleep(500);
            } catch (Exception ignored) {
                System.out.println("[SERVER] Server is awoken from being asleep!");
            }
        }
    }
}
