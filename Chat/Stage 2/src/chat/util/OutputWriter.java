package chat.util;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class OutputWriter implements Runnable {
    private final Socket socket;
    private DataOutputStream dataOutputStream;

    public OutputWriter(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        createOutputStream();
        if (!isHasStream()) {
            return;
        }
        sent();

    }

    private void sent() {
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextLine()) {
                if (!sentMessage(scanner.nextLine()))
                    return;
            }
        }
    }

    private boolean isHasStream() {
        if (dataOutputStream == null) {
            System.out.println("input reader is aborted");
            return false;
        }
        return true;
    }

    private boolean sentMessage(final String message) {
        try {
            dataOutputStream.writeUTF(message);
        } catch (EOFException | SocketException ignored) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("can't write the message");
        }
        return true;
    }

    private void createOutputStream() {
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("outputWriter can't create a output stream");
        }

    }

}
