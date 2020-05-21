package chat.server;

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
        registrationOrLogin();
        chatting();
    }

    private void registrationOrLogin() {
        while (!ioManager.isSocketClosed()) {
            final String tempInput = ioManager.read();


            if (chatData.registry(this, tempInput)) {
                this.userName = tempInput;
                ioManager.sent("welcome");
                final String temp = chatData.getLastMessages();
                if (!temp.isEmpty())
                    ioManager.sent(temp);
                break;
            } else {
                ioManager.sent("Server: This name is in use! Choose another one:");
            }
        }
    }

    private void chatting() {
        while (!ioManager.isSocketClosed()) {
            final String tempMessage = ioManager.read();
            if (tempMessage.equals("/exit")) {
                chatData.unRegistry(userName);
                ioManager.closeSocket();
                return;
            } else {
                chatData.addMessage(userName + ": " + tempMessage);
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
