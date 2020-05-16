package chat.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket ss;

    public static void main(final String[] args) {
        new Server().start();
    }

    private void start() {
        System.out.println("Server started!");
        createServerSocket();
        createSocketDealer();
        pause();
        closeSocket();
    }

    private void pause() {
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {
        }
    }

    private void closeSocket() {
        try {
            ss.close();
        } catch (Exception ignored) {
        }
    }

    private void createSocketDealer() {
        new Thread(() -> {
            while (!ss.isClosed()) {
                final Socket clientSocket = getConnection();
                if (clientSocket != null)
                    new Thread(new ConnectionProcess(clientSocket)).start();
            }
        }).start();

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
        final String address = "127.0.0.1";
        final int port = 23456;
        while (true) {
            try {
                ss = new ServerSocket(port, 50, InetAddress.getByName(address));
                return;
            } catch (Exception ignored) {
                System.out.println("[SERVER] Can't create a socket!");
            }
            try {
                Thread.sleep(10);
            } catch (Exception ignored) {
                System.out.println("[SERVER] Server is awoken from being asleep!");
            }
        }
    }

}
