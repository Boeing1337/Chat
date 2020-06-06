package chat.server;

import chat.server.util.UserInfo;
import chat.server.util.UsersDataManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChatProcessor {
    private final Set<String> allUsers = new HashSet<>();
    private final Map<String, Client> onlineUsers = new HashMap<>();
    private final Set<String> conversations = new HashSet<>();
    private final UsersDataManager usersDataManager;

    ChatProcessor() {
        this.usersDataManager = new UsersDataManager(allUsers);
        usersDataManager.createUser("admin", "12345678", "1");
    }

    synchronized void revoke(final String owner, final String target) {

    }

    synchronized void kick(final String owner, final String target) {

    }

    synchronized void grant(final Client owner, final String target) {

    }

    synchronized String getStats(final String owner, final String fromUser) {

    }

    synchronized boolean registry(final Client client, final String login,
                                  final String pass) {

        if (allUsers.contains(login)) {
            client.sentTechnicalMessage("this login is already in use!");
            return false;
        }
        if (pass.length() < 8) {
            client.sentTechnicalMessage("the password is too short!");
            return false;
        }

        usersDataManager.createUser(login, pass, "3");
        client.sentTechnicalMessage("you are registered successfully!");
        return true;
    }

    synchronized boolean auth(final Client client, final String login,
                              final String pass) {

        if (!usersDataManager.isUserExist(login)) {
            client.sentTechnicalMessage("incorrect login!");
            return false;
        }
        final UserInfo userInfo = usersDataManager.getUserInfo(login);
        if (!userInfo.getLogin().equals(pass)) {
            client.sentTechnicalMessage("incorrect password!");
            return false;
        }
        client.sentTechnicalMessage("you are authorized successfully!");
        return true;
    }

    synchronized void sentMessage(final String fromUser, final String toUser,
                                  final String message) {

        usersDataManager.saveMessage(fromUser, toUser, message);

        if (conversations.contains(toUser + fromUser))
            onlineUsers.get(toUser).sentMessage(fromUser + ": " + message);


    }

    synchronized String getLastMessages(final String user, final String fromUser) {

        return usersDataManager.getLastMessages(user, fromUser);

    }

    synchronized void logOut(final String fromUser, final String toUser) {
        conversations.remove(fromUser + toUser);
        onlineUsers.remove(fromUser);
    }

    synchronized void setConversation(final String fromUser, final String toUser) {
        conversations.remove(fromUser + toUser);
        conversations.add(fromUser + toUser);
    }

    boolean isAddresseeOnline(final String target) {
        return onlineUsers.get(target) != null;
    }

    synchronized void setOnline(final String login, final Client client) {
        onlineUsers.put(login, client);
    }

    String getOnlineUsers(final String owner) {
        final StringBuilder temp = new StringBuilder();
        final Set<String> set = new HashSet<>(onlineUsers.keySet());
        set.remove(owner);
        set.forEach(a -> temp.append(" ").append(a));
        final String users = temp.toString().trim();
        if (users.isEmpty()) {
            return "no one online";
        } else {
            return "online: " + users;
        }
    }

}