package chat.server;

import chat.server.util.Message;

import java.util.HashMap;
import java.util.Map;

public class ChatData {
    private final Map<String, UserThread> onlineUsers = new HashMap<>();
    private final Map<String, String> allUsers = new HashMap<>();

    synchronized boolean registry(final UserThread userThread, final String login,
                                  final String pass) {

        if (allUsers.get(login) != null) {
            userThread.sentMessage("This login is already in use!");
            return false;
        }
        if (pass.length() < 8) {
            userThread.sentMessage("The password is too short");
            return false;
        }

        allUsers.put(login, pass);
        onlineUsers.put(login, userThread);
        return true;
    }

    synchronized boolean auth(final UserThread userThread, final String login,
                              final String pass) {

        final String tempPass = allUsers.get(login);

        if (tempPass == null || !tempPass.equals(pass)) {
            userThread.sentMessage("Wrong users' data!");
            return false;
        }

        onlineUsers.put(login, userThread);
        return true;
    }

    synchronized void sentMessage(final UserThread userThread, final Message message) {

    }

    synchronized void getLastMessages() {

    }


}
