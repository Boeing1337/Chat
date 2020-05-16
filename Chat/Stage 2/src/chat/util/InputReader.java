package chat.util;

import java.io.DataInputStream;
import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;

public class InputReader implements Runnable {
    private final Socket socket;
    private DataInputStream dataInputStream;

    public InputReader(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        createInputStream();
        if (!isHasStream()) {
            return;
        }
        readAndOutputMessage();
    }

    private boolean isHasStream() {
        if (dataInputStream == null) {
            System.out.println("input reader is aborted");
            return false;
        }
        return true;
    }

    private void readAndOutputMessage() {
        while (true) {
            try {
                String msg = dataInputStream.readUTF();
                System.out.println(msg);
            } catch (EOFException | SocketException ignored) {
                return;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("can't read the message");
            }
        }
    }

    private void createInputStream() {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("inputReader can't create a input stream");
        }
    }
}
