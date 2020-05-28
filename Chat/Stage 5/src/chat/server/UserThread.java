package chat.server;

import chat.server.util.Message;

import java.net.Socket;

public class UserThread implements Runnable {
    private final ChatData chatData;
    private final IOManager ioManager;
    private String userName;
    private String addressee;

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
        while (!ioManager.isSocketClosed()) {
            Message message = new Message(ioManager.read());

            switch (message.getCommand()) {
                case "list":
                    ioManager.sent(chatData.getOnlineUsers());
                    break;
                case "chat":
                    if (chatData.isAddresseeOnline(message.getTarget())) {
                        chatData.removeConversation(userName, addressee);
                        chatData.creatConversation(userName, addressee);
                        chatData.getLastMessages(userName, addressee);
                        addressee = message.getTarget();
                    } else {
                        ioManager.sent("User is not online!");
                    }
                    break;
                case "exit":
                    chatData.removeConversation(userName, addressee);
                    chatData.logOut(userName);
                    ioManager.closeSocket();
                    break;
                default:
                    if (addressee.isEmpty()) {
                        ioManager.sent("Use /list command to choose an user to text!");
                    } else {
                        chatData.sentMessage(userName, addressee, message.getMessage());
                    }
                    break;
            }
        }
    }

    private void authOrRegister() {
        while (!ioManager.isSocketClosed()) {
            Message message = new Message(ioManager.read());

            switch (message.getCommand()) {
                case "registration":
                    if (chatData.registry(this, message.getLogin(), message.getPass())) {
                        userName = message.getLogin();
                        return;
                    }
                    break;
                case "auth":
                    if (chatData.auth(this, message.getLogin(), message.getPass())) {
                        userName = message.getLogin();
                        return;
                    }
                    break;
                case "exit":
                    ioManager.closeSocket();
                    return;
                default:
                    ioManager.sent("You are not in the chat!");
                    break;
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


