package chat.server;

import chat.connection.InputReader;
import chat.connection.OutputWriter;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public class IOManager {
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private final Socket socket;

    IOManager(final Socket socket) {
        this.socket = socket;
        this.inputReader = new InputReader(socket);
        this.outputWriter = new OutputWriter(socket);
    }

    boolean isSocketClosed() {
        return socket.isClosed();
    }

    void closeSocket() {
        try {
            socket.close();
        } catch (Exception ignored) {
        }
    }

    String read() {
        return inputReader.read().trim();
    }

    void sent(@NotNull final String message) {
        outputWriter.sentMessage("Server: " + message);
    }


}
