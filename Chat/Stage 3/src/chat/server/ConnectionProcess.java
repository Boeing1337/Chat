package chat.server;

import java.net.Socket;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class ConnectionProcess implements Runnable {
    private final List<String> messageStore;
    private final Queue<String> inputMessages = new ArrayDeque<>();
    private final Queue<String> outputMessages = new ArrayDeque<>();
    private final Socket socket;

    public ConnectionProcess(final Socket socket, final List<String> messageStore) {
        this.socket = socket;
        this.messageStore = messageStore;
    }


    @Override
    public void run() {
        createIOWorkers();
        processMessages();
    }

    private void processMessages() {
        while (!socket.isClosed() && socket.isConnected()) {
            final String tempMessage = inputMessages.peek();
            if (tempMessage != null) {
                if (tempMessage.equals("/exit")) {
                    closeSocket();
                    return;
                } else {
                    messageStore.add(tempMessage);
                    outputMessages.add(countWords(tempMessage));
                    inputMessages.remove();
                }
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

    private void createIOWorkers() {
        createInputReader();
        createOutputWriter();
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (Exception ignored) {
        }
    }

    private void createInputReader() {
        new Thread(new InputReader(socket, inputMessages)).start();
    }

    private void createOutputWriter() {
        new Thread(new OutputWriter(socket, outputMessages)).start();
    }
}
