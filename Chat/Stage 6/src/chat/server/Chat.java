package chat.server;

import chat.server.DAO.Cybernate;
import chat.server.util.UserInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Chat {
    private final Set<String> allUsers;
    private final Map<String, UserThread> onlineUsers = new HashMap<>();
    private final Set<String> conversations = new HashSet<>();
    private final Cybernate cybernate;

    public Chat() {
        allUsers = new HashSet<>();
        this.cybernate = new Cybernate(allUsers);
        cybernate.createUser("admin", "12345678", "1");
    }

    public synchronized void getHistory(final String owner, final String fromUser) {
        cybernate.getLastMessages(owner, fromUser);
    }

    public synchronized void revoke(final String owner, final String fromUser) {

    }

    public synchronized void kick(final UserThread admin, final String target) {
        if (admin.getLogin().equals(target)) {
            admin.sentTechnicalMessage("you can't kick himself!");
            return;
        }

        final UserThread kicked = onlineUsers.get(target);
        if (kicked == null)
            return;

        if (kicked.getRights() <= admin.getRights())
            return;


        onlineUsers.remove(target);
        kicked.sentTechnicalMessage("you have been kicked from the server!");
        kicked.setState(UserThread.State.OFFLINE);
        admin.sentTechnicalMessage(target + " was kicked!");
    }

    public synchronized void grant(final String owner, final String fromUser) {

    }

    public synchronized void getUnreadUsers(final String owner) {

    }

    public synchronized void getStats(final String owner, final String fromUser) {

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

        cybernate.createUser(login, pass, "3");
        finishAuth(userThread, login, 3);
        userThread.sentTechnicalMessage("you are registered successfully!");
    }

    public synchronized void auth(final UserThread userThread, final String login,
                                  final String pass) {

        if (!allUsers.contains(login)) {
            userThread.sentTechnicalMessage("incorrect login!");
            return;
        }
        final UserInfo userInfo = cybernate.getUserInfo(login);

        if (!userInfo.getPassword().equals(pass)) {
            userThread.sentTechnicalMessage("incorrect password!");
            return;
        }

        if (userInfo.getBanTime() > System.currentTimeMillis()) {
            userThread.sentTechnicalMessage("you are banned!");
            return;
        }

        finishAuth(userThread, login, userInfo.getRights());
        userThread.sentTechnicalMessage("you are authorized successfully!");
    }

    private synchronized void finishAuth(final UserThread userThread, final String login,
                                         final int rights) {

        userThread.setState(UserThread.State.ONLINE);
        onlineUsers.put(login, userThread);
        userThread.setLogin(login);
        userThread.setRights(rights);
    }


    public synchronized void sentMessage(final String owner, final String toUser,
                                         String message) {
        message = owner + ": " + message;
        if (conversations.contains(toUser + owner)) {
            cybernate.saveAsReadMessage(toUser, owner, message);
            onlineUsers.get(toUser).sentMessage(message);
        } else {
            cybernate.saveAsUnreadMessage(toUser, owner, message);
        }

        cybernate.saveAsReadMessage(owner, toUser, message);
        onlineUsers.get(owner).sentMessage(message);

    }

    public synchronized void logOut(final String fromUser, final String toUser) {
        conversations.remove(fromUser + toUser);
        onlineUsers.remove(fromUser);
    }

    public synchronized void setConversation(final UserThread userThread, final String toUser) {
        if (onlineUsers.get(toUser) == null) {
            userThread.sentTechnicalMessage("the user is not online!");
            return;
        }

        final String fromUser = userThread.getLogin();
        conversations.remove(fromUser + userThread.getAddressee());
        conversations.add(fromUser + toUser);
        cybernate.createConversation(fromUser, toUser);
        userThread.setState(UserThread.State.CONVERSATION);
        userThread.setAddressee(toUser);
        final String messages = cybernate.getLastMessages(fromUser, toUser);
        if (!messages.isEmpty())
            userThread.sentMessage(messages);
    }

    public void sendOnlineUsers(final UserThread owner) {
        final StringBuilder temp = new StringBuilder();
        final Set<String> set = new HashSet<>(onlineUsers.keySet());
        set.remove(owner.getLogin());
        set.forEach(a -> temp.append(" ").append(a));
        final String users = temp.toString().trim();
        if (users.isEmpty()) {
            owner.sentTechnicalMessage("no one online");
        } else {
            owner.sentTechnicalMessage("online: " + users);
        }
    }

}