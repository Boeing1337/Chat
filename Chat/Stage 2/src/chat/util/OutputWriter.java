package chat.util;

import java.io.DataOutputStream;
import java.net.Socket;

public class OutputWriter implements Runnable {
    private final Socket socket;
    private MessageContainer outputMessage;
    private DataOutputStream dataOutputStream;

    public OutputWriter(final Socket socket, final MessageContainer outputMessage) {
        this.socket = socket;
        this.outputMessage = outputMessage;
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
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("sent method has been interrupted");
                e.printStackTrace();
            }

            if (!outputMessage.isEmpty()) {
                sentMessage();
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

    private void sentMessage() {
        try {
            dataOutputStream.writeUTF(outputMessage.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("can't write the message");
        } finally {
            outputMessage.setMessage("");
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
