package chat.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Server {
    private final List<String> messageStore = Collections.synchronizedList(new ArrayList<>());
    private ServerSocket ss;
    private Thread socketDealer;

    public static void main(final String[] args) {
        new Server().start();
    }

    private void start() {
        createServerSocket();
        createSocketDealer();
        scannerWork();

    }

    private void scannerWork() {
        final Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            final String temp = scanner.nextLine().trim();
            if (temp.equals("/exit")) {
                socketDealer.interrupt();
                closeSocket();
                return;
            } else if (temp.equals("/show")) {
                showAllMessages();
            }
        }
    }

    private void showAllMessages() {
        messageStore.forEach(System.out::println);
    }

    private void closeSocket() {
        try {
            ss.close();
        } catch (Exception ignored) {

        }
    }

    private void createSocketDealer() {
        socketDealer = new Thread(() -> {
            while (!ss.isClosed()) {
                final Socket clientSocket = getConnection();
                if (clientSocket != null)
                    new Thread(new ConnectionProcess(clientSocket, messageStore)).start();
            }
        });
        socketDealer.start();
    }


    private Socket getConnection() {
        try {
            return ss.accept();
        } catch (Exception e) {
            System.out.println("[SERVER] Can't accept a connection!");
        }
        return null;
    }

    private void createServerSocket() {
        for (int tries = 0; tries <= 20; tries++) {
            sleep500();
            if (tryCreateServerSocket())
                return;
            if (tries == 20)
                throw new RuntimeException("[SERVER] Can't create the ServerSocket");

        }
    }

    private boolean tryCreateServerSocket() {
        final String address = "127.0.0.1";
        final int port = 23456;
        try {
            ss = new ServerSocket(port, 50, InetAddress.getByName(address));
            return true;
        } catch (Exception ignored) {
            System.out.println("[SERVER] Can't create a socket!");
            return false;
        }
    }

    private void sleep500() {
        try {
            Thread.sleep(500);
        } catch (Exception ignored) {
            System.out.println("[SERVER] Server is awoken from being asleep!");
        }
    }
}
