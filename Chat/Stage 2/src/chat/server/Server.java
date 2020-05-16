package chat.server;

import chat.util.InputReader;
import chat.util.OutputWriter;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket ss;

    public static void main(final String[] args) {
        new Server().start();
    }

    private void start() {
        createServerSocket();
        createIOWorkersThread();
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

    private void createIOWorkersThread() {
        new Thread(() -> {
            while (!ss.isClosed()) {
                final Socket clientSocket = getConnection();
                if (clientSocket != null) {
                    createInputReader(clientSocket);
                    createOutputWriter(clientSocket);
                }
            }
        }).start();
    }

    private Socket getConnection() {
        try {
            return ss.accept();
        } catch (Exception ignored) {
        }
        return null;
    }

    private void createInputReader(final Socket clientSocket) {
        new Thread(new InputReader(clientSocket)).start();
    }

    private void createOutputWriter(final Socket clientSocket) {
        new Thread(new OutputWriter(clientSocket)).start();
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
