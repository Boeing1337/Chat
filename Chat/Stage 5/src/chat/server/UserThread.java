package chat.server;

import chat.server.util.Message;

import java.net.Socket;

public class UserThread implements Runnable {
    private final ChatData chatData;
    private final IOManager ioManager;
    private String userName;

    public UserThread(final Socket socket, final ChatData chatData) {
        this.chatData = chatData;
        this.ioManager = new IOManager(socket);
    }

    @Override
    public void run() {
        sendDefaultMessage();
        authOrRegister();
        chatting();
    }

    private void chatting() {

    }

    private void authOrRegister() {
        while (true) {
            Message message = new Message(ioManager.read());

            if (message.getCommand().equals("registration")) {
                if (chatData.registry(this, message.getLogin(), message.getPass())) {
                    userName = message.getLogin();
                    return;
                }
            } else if (message.getCommand().equals("auth")) {
                if (chatData.auth(this, message.getLogin(), message.getPass())) {
                    userName = message.getLogin();
                    return;
                }
            } else {
                ioManager.sent("You are not in the chat!");
            }
        }

    }


    private void sendDefaultMessage() {
        ioManager.sent("Server: authorize or register.");
    }

    void sentMessage(final String message) {
        ioManager.sent(message);
    }


}


