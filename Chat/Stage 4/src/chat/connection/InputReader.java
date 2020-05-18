package chat.connection;

import java.io.DataInputStream;
import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;

public class InputReader {
    private final Socket socket;
    private DataInputStream dataInputStream;

    public InputReader(final Socket socket) {
        this.socket = socket;
        createInputStream();
    }

    public String read() {
        try {
            return dataInputStream.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("can't read the message");
        }
        return "";
    }

    private void createInputStream() {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("inputReader can't create a input stream");
        }

    }
}
