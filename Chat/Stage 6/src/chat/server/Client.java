package chat.server;

import chat.server.util.Message;

import java.net.Socket;

public class Client implements Runnable {
    private final ChatProcessor chatProcessor;
    private final IOManager ioManager;
    private State state = State.OFFLINE;
    private Message message;
    private String login;
    private String addressee;

    public Client(final Socket socket, final ChatProcessor chatProcessor) {
        this.chatProcessor = chatProcessor;
        this.ioManager = new IOManager(socket);
    }

    @Override
    public void run() {
        sendDefaultMessage();
        authOrRegister();
        postAuthentication();
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
                case "stats":
                    sendStatsOfConversation();
                    break;
                case "kick":
                    kickUser();
                    break;
                case "grant":
                    grantRightsToUser();
                    break;
                case "revoke":
                    removeRightsFromUser();
                    break;
                case "unread":
                    sendUnreadList();
                    break;
                case "history":
                    sendHistory();
                    break;
                case "text":
                    tryToSentMessage();
                    break;
            }
        }
    }

    private void kickUser() {
        chatProcessor.kick(login, message.getTarget());
    }

    private void sendStatsOfConversation() {
        if (state == State.CONVERSATION)
            sentMessage((chatProcessor.getStats(login, addressee)));
        else
            sentTechnicalMessage(
            "You need to choose a conversation before using this command");
    }

    private void authOrRegister() {
        while (!ioManager.isSocketClosed()) {
            message = new Message(ioManager.read());

            switch (message.getCommand()) {
                case "registration":
                    if (chatProcessor.registry(this, message.getLogin(), message.getPass()))
                        return;
                    break;
                case "auth":
                    if (chatProcessor.auth(this, message.getLogin(), message.getPass()))
                        return;
                    break;
                case "exit":
                    ioManager.closeSocket();
                    return;
                default:
                    sentTechnicalMessage("you are not in the chat!");
                    break;
            }
        }
        state = State.ONLINE;
    }

    private void postAuthentication() {
        chatProcessor.setOnline(login, this);
        login = message.getLogin();
    }

    private void tryToSentMessage() {
        if (addressee.isEmpty()) {
            sentTechnicalMessage("use /list command to choose an user to text!");
        } else {
            chatProcessor.sentMessage(login, addressee, message.getMessage());
            sentMessage(login + ": " + message.getMessage());
        }
    }

    private void sendListOfUsers() {
        sentTechnicalMessage(chatProcessor.getOnlineUsers(login));
    }

    private void exit() {
        chatProcessor.logOut(login, addressee);
        ioManager.closeSocket();
    }

    private void setConversation() {
        if (chatProcessor.isAddresseeOnline(message.getTarget())) {
            state = State.CONVERSATION;
            addressee = message.getTarget();
            chatProcessor.setConversation(login, addressee);
            final String temp = chatProcessor.getLastMessages(login, addressee);
            if (!temp.isEmpty())
                sentMessage(temp);
            addressee = message.getTarget();
        } else {
            sentTechnicalMessage("the user is not online!");
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

    enum State {
        OFFLINE,
        ONLINE,
        CONVERSATION
    }

}


