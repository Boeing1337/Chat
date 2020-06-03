package chat.server;

import chat.server.util.Message;

import java.net.Socket;

public class UserThread implements Runnable {
    private final ChatData chatData;
    private final IOManager ioManager;
    private Message message;
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
            message = new Message(ioManager.read().trim());

            switch (message.getCommand()) {
                case "list":
                    sendListOfUsers();
                    break;
                case "chat":
                    setConversation();
                    break;
                case "exit":
                    exit();
                    break;
                case "text":
                    tryToSentMessage();
                    break;
            }
        }
    }

    private void tryToSentMessage() {
        if (addressee.isEmpty()) {
            sentTechnicalMessage("use /list command to choose an user to text!");
        } else {
            chatData.sentMessage(userName, addressee, message.getMessage());
            sentMessage(userName + ": " + message.getMessage());
        }
    }

    private void sendListOfUsers() {
        sentTechnicalMessage(chatData.getOnlineUsers(userName));
    }

    private void exit() {
        chatData.logOut(userName, addressee);
        ioManager.closeSocket();
    }

    private void setConversation() {
        if (chatData.isAddresseeOnline(message.getTarget())) {
            addressee = message.getTarget();
            chatData.setConversation(userName, addressee);
            final String temp = chatData.getLastMessages(addressee, userName);
            if (!temp.isEmpty())
                sentMessage(temp);
            addressee = message.getTarget();
        } else {
            sentTechnicalMessage("the user is not online!");
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
                    sentTechnicalMessage("you are not in the chat!");
                    break;
            }
        }

    }

    private void sendDefaultMessage() {
        sentTechnicalMessage("authorize or register.");
    }

    void sentTechnicalMessage(final String message) {
        ioManager.sent("Server: " + message);
    }

    void sentMessage(final String message) {
        ioManager.sent(message);
    }


}


