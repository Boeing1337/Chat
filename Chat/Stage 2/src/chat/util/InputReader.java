package chat.util;

import java.io.DataInputStream;
import java.net.Socket;

public class InputReader implements Runnable {
    private final Socket socket;
    private MessageContainer inputMessage;
    private DataInputStream dataInputStream;

    public InputReader(final Socket socket, final MessageContainer inputMessage) {
        this.socket = socket;
        this.inputMessage = inputMessage;
    }

    @Override
    public void run() {
        createInputStream();
        if (!isHasStream()) {
            return;
        }
        readMessage();
    }

    private boolean isHasStream() {
        if (dataInputStream == null) {
            System.out.println("input reader is aborted");
            return false;
        }
        return true;
    }


    private void readMessage() {
        try {
            String msg = dataInputStream.readUTF();
            inputMessage.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("can't read the message");
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
