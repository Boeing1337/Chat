package chat.server;

import chat.server.DAO.Cybernate;
import chat.server.util.DialogStats;
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

    public synchronized void revoke(final String target) {
        final UserThread admin = onlineUsers.get("admin");
        if (admin == null)
            return;
        if (!allUsers.contains(target))
            return;

        UserInfo userInfo = cybernate.getUserInfo(target);
        userInfo.revoke();
        cybernate.saveUserInfo(target, userInfo);
        final UserThread userThread = onlineUsers.get(target);
        if (userThread != null) {
            userThread.setRights(3);
            userThread.sentTechnicalMessage("you are no longer a moderator!");
        }

        admin.sentTechnicalMessage(target + " is no longer a moderator!");
    }

    public synchronized void kick(final UserThread admin, final String target) {
        if (admin.getLogin().equals(target)) {
            admin.sentTechnicalMessage("you can't kick himself!");
            return;
        }

        final UserThread kicked = onlineUsers.get(target);
        if (kicked == null)
            return;

        if (admin.getRights() >= kicked.getRights())
            return;


        onlineUsers.remove(target);
        kicked.sentTechnicalMessage("you have been kicked from the server!");
        kicked.setState(UserThread.State.OFFLINE);
        UserInfo userInfo = cybernate.getUserInfo(target);
        userInfo.ban();
        cybernate.saveUserInfo(target, userInfo);
        admin.sentTechnicalMessage(target + " was kicked!");
    }

    public synchronized void grant(final String user, final UserThread admin) {
        if (!allUsers.contains(user))
            return;

        UserInfo userInfo = cybernate.getUserInfo(user);
        if (userInfo.getRights() == 2) {
            admin.sentTechnicalMessage("this user is already a moderator!");
            return;
        }

        userInfo.grant();
        cybernate.saveUserInfo(user, userInfo);

        final UserThread userThread = onlineUsers.get(user);
        if (userThread != null) {
            userThread.setRights(2);
            userThread.sentTechnicalMessage("you are a new moderator now!");
        }
        admin.sentTechnicalMessage(userInfo.getLogin() + " is a new moderator!");
    }

    public synchronized void getUnread(final UserThread owner) {
        final String unread = cybernate.parseUnread(owner.getLogin());
        if (unread.isEmpty()) {
            owner.sentTechnicalMessage("no one unread");
        } else {
            owner.sentTechnicalMessage("unread from: " + unread);
        }

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

        if (userInfo.getBanTime() > 0) {
            userThread.sentTechnicalMessage("you are banned!");
            return;
        }

        finishAuth(userThread, login, userInfo.getRights());
        userThread.sentTechnicalMessage("you are authorized successfully!");
    }

    private synchronized void finishAuth(final UserThread userThread,
                                         final String login,
                                         final int rights) {

        userThread.setState(UserThread.State.ONLINE);
        onlineUsers.put(login, userThread);
        userThread.setLogin(login);
        userThread.setRights(rights);
    }


    public synchronized void sentMessage(final String owner, final String toUser,
                                         final String message) {
        final DialogStats ownerStats = cybernate.getDialogStats(owner, toUser);
        final DialogStats toUserStats = cybernate.getDialogStats(toUser, owner);
        cybernate.saveMessage(owner, toUser, message);
        cybernate.saveMessage(toUser, owner, message);

        if (conversations.contains(toUser + owner)) {
            toUserStats.increaseRead(owner, toUser);
            onlineUsers.get(toUser).sentMessage(message);
        } else {
            toUserStats.increaseUnread();
        }
        cybernate.saveConversationInfo(toUser, owner, toUserStats);
        ownerStats.increaseRead(owner, owner);
        cybernate.saveConversationInfo(owner, toUser, ownerStats);
        onlineUsers.get(owner).sentMessage(message);
    }


    public synchronized void logOut(final String fromUser, final String toUser) {
        conversations.remove(fromUser + toUser);
        onlineUsers.remove(fromUser);
    }

    public synchronized void setConversation(final UserThread userThread,
                                             final String toUser) {
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