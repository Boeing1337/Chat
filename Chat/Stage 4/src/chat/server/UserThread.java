package chat.server;

import chat.connection.InputReader;
import chat.connection.OutputWriter;

import java.net.Socket;

public class UserThread implements Runnable {
    private final ChatData chatData;
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private final Socket socket;
    private String userName;

    public UserThread(final Socket socket, final ChatData chatData) {
        this.chatData = chatData;
        this.socket = socket;
        this.inputReader = new InputReader(socket);
        this.outputWriter = new OutputWriter(socket);
    }

    @Override
    public void run() {
        sendDefaultMessage();
        registration();
        chatting();
    }

    private void registration() {
        while (!socket.isClosed()) {
            final String tempUserName = inputReader.read().trim();

            if (chatData.registry(this, tempUserName)) {
                this.userName = tempUserName;
                final String temp = chatData.getLastMessages();
                if (!temp.isEmpty())
                    outputWriter.sentMessage(temp);
                break;
            } else {
                outputWriter.sentMessage("Server: This name is in use! Choose another " +
                "one");
            }
        }
    }

    private void chatting() {
        while (!socket.isClosed()) {
            final String tempMessage = inputReader.read().trim();
            if (tempMessage.equals("/exit")) {
                chatData.unRegistry(userName);
                closeSocket();
                return;
            } else {
                chatData.addMessage(userName + ": " + tempMessage);
            }
        }
    }


    private void sendDefaultMessage() {
        outputWriter.sentMessage("Server: write your name");
    }

    protected void sentMessage(final String message) {
        outputWriter.sentMessage(message);
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (Exception ignored) {
        }
    }

}
