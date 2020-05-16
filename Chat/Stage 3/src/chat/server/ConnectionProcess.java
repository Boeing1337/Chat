package chat.server;

import chat.connection.InputReader;
import chat.connection.OutputWriter;

import java.net.Socket;

public class ConnectionProcess implements Runnable {
    private static int clientNumber = 0;
    private final int client;
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private final Socket socket;

    public ConnectionProcess(final Socket socket) {
        this.client = ++clientNumber;
        System.out.printf("Client %s connected!\n", client);
        this.socket = socket;
        this.inputReader = new InputReader(socket);
        this.outputWriter = new OutputWriter(socket);
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            final String rawMessage = inputReader.read().trim();
            if ("/exit".equals(rawMessage)) {
                System.out.printf("Client %s disconnected!\n", client);
                closeSocket();
                return;
            } else {
                System.out.printf("Client %s sent: %s\n", client, rawMessage);
                final String outputMessage = countWords(rawMessage);
                System.out.printf("Sent to client %s: %s\n", client, outputMessage);
                outputWriter.sentMessage(outputMessage);
            }

        }
    }

    private String countWords(final String message) {
        return "Count is " + (message.trim().isEmpty() ? "0" :
        message
        .replaceAll("\\s+", " ")
        .split("\\s")
        .length);
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (Exception ignored) {
        }
    }


}
