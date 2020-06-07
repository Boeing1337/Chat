package chat.server;

import chat.server.DAO.DataRetriever;
import chat.server.util.UserInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Chat {
    private final Set<String> allUsers;
    private final Map<String, UserThread> onlineUsers = new HashMap<>();
    private final Set<String> conversations = new HashSet<>();
    private final DataRetriever dataRetriever;

    public Chat() {
        allUsers = new HashSet<>();
        this.dataRetriever = new DataRetriever(allUsers);
        dataRetriever.createUser("admin", "12345678", "1");
    }

    public synchronized String getHistory(final String owner, final String fromUser) {
        dataRetriever.getLastMessages(owner, fromUser);
    }

    public synchronized void revoke(final String owner, final String fromUser) {

    }

    public synchronized void kick(final String target) {

    }

    public synchronized void grant(final String owner, final String fromUser) {

    }

    public synchronized String getUnreadUsers(final String owner) {

    }

    public synchronized String getStats(final String owner, final String fromUser) {

    }

    public synchronized void registry(final UserThread userThread,
                                      final String login,
                                      final String pass) {

        if (allUsers.contains(login)) {
            userThread.sentTechnicalMessage("this login is already in use!");
            return;
        }
        if (pass.length() < 8) {
            userThread.sentTechnicalMessage("the password is too short!");
            return;
        }

        dataRetriever.createUser(login, pass, "3");
        finishLogging(userThread, login, 3);
        userThread.sentTechnicalMessage("you are registered successfully!");
    }

    private synchronized void finishLogging(final UserThread userThread, final String login,
                                            final int rights) {

        userThread.setState(UserThread.State.ONLINE);
        onlineUsers.put(login, userThread);
        userThread.setLogin(login);
        userThread.setRights(rights);
    }

    public synchronized void auth(final UserThread userThread, final String login,
                                  final String pass) {

        if (!dataRetriever.isUserExist(login)) {
            userThread.sentTechnicalMessage("incorrect login!");
            return;
        }
        final UserInfo userInfo = dataRetriever.getUserInfo(login);
        if (!userInfo.getLogin().equals(pass)) {
            userThread.sentTechnicalMessage("incorrect password!");
            return;
        }

        finishLogging(userThread, login, userInfo.getRights());
        userThread.sentTechnicalMessage("you are authorized successfully!");
    }

    public synchronized void sentMessage(final String fromUser, final String owner,
                                         final String message) {

        if (conversations.contains(owner + fromUser)) {
            dataRetriever.saveAsReadMessage(owner, fromUser, message);
            onlineUsers.get(owner).sentMessage(fromUser + ": " + message);
        } else {
            dataRetriever.saveAsUnreadMessage(owner, fromUser, message);
        }

    }

    public synchronized String getLastMessages(final String user, final String fromUser) {

        return dataRetriever.getLastMessages(user, fromUser);

    }

    public synchronized void logOut(final String fromUser, final String toUser) {
        conversations.remove(fromUser + toUser);
        onlineUsers.remove(fromUser);
    }

    public synchronized void setConversation(final String fromUser, final String toUser) {
        conversations.remove(fromUser + toUser);
        conversations.add(fromUser + toUser);
    }

    public boolean isAddresseeOnline(final String target) {
        return onlineUsers.get(target) != null;
    }

    public synchronized void setOnline(final String login, final UserThread userThread) {
        onlineUsers.put(login, userThread);
    }

    public String getOnlineUsers(final String owner) {
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