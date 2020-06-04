package chat.connection;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;

public class OutputWriter {
    private final Socket socket;
    private DataOutputStream dataOutputStream;


    public OutputWriter(final Socket socket) {
        this.socket = socket;
        createOutputStream();
    }

    public void sentMessage(final String message) {
        try {
            dataOutputStream.writeUTF(message);
        } catch (EOFException | SocketException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("can't write the message");
        }
    }

    private void createOutputStream() {
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("outputWriter can't create a output stream");
        }

    }

}
