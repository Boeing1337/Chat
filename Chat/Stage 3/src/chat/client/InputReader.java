package chat.client;

import java.io.DataInputStream;
import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class InputReader implements Runnable {
    private final Socket socket;
    private final List<String> inputMessages = new ArrayList<>();
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
        readMessage();
    }

    public List<String> getAllMessages() {
        return inputMessages;
    }

    private boolean isHasStream() {
        if (dataInputStream == null) {
            System.out.println("input reader is aborted");
            return false;
        }
        return true;
    }


    private void readMessage() {
        while (!socket.isClosed() && socket.isConnected()) {
            sleep500();
            read();
        }
    }

    private void read() {
        try {
            final String msg = dataInputStream.readUTF();
            inputMessages.add(msg);
        } catch (EOFException | SocketException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("can't read the message");
        }
    }

    private void sleep500() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("sent method has been interrupted");
            e.printStackTrace();
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
