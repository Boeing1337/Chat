package chat.server;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChatData {
    private final Map<String, UserThread> onlineUsers = new HashMap<>();
    private final Map<String, String> allUsers = new HashMap<>();
    private final Set<String> conversations = new HashSet<>();

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
        try {
            Files.createDirectories(Paths.get(login));
        } catch (Exception ignored) {
        }
        return true;
    }

    synchronized boolean auth(final UserThread userThread, final String login,
                              final String pass) {

        final String tempPass = allUsers.get(login);

        if (tempPass == null) {
            userThread.sentMessage("Incorrect login!");
            return false;
        }
        if (!tempPass.equals(pass)) {
            userThread.sentMessage("Incorrect password!");
            return false;
        }

        onlineUsers.put(login, userThread);
        return true;
    }

    synchronized void sentMessage(final String fromUser, final String toUser,
                                  final String message) {
        if (conversations.contains(fromUser + toUser))
            onlineUsers.get(toUser).sentMessage(message);

        saveMessage(fromUser, toUser, message);

    }

    private void saveMessage(final String fromUser, final String toUser,
                             final String message) {
        if (!isFileExist(fromUser, toUser))
            createFile(fromUser, toUser);

        try (FileWriter file = new FileWriter(toUser + "/" + fromUser, true)) {
            file.write("\n" + message);
        } catch (Exception ignored) {

        }

    }

    private void createFile(final String fromUser, final String toUser) {
        try {
            new File(toUser + "/" + fromUser).createNewFile();
        } catch (Exception ignored) {
        }
    }

    private boolean isFileExist(final String fromUser, final String toUser) {
        return new File(toUser + "/" + fromUser).exists();
    }

    synchronized void getLastMessages(final String fromUser, final String toUser) {

    }

    synchronized void logOut(final String userName) {
        onlineUsers.remove(userName);
    }

    synchronized void creatConversation(final String fromUser, final String toUser) {
        conversations.add(fromUser + toUser);
    }

    synchronized void removeConversation(final String fromUser, final String toUser) {
        conversations.remove(fromUser + toUser);
    }

    boolean isAddresseeOnline(final String target) {
        return onlineUsers.get(target) != null;
    }

    String getOnlineUsers() {
        final StringBuilder temp = new StringBuilder();
        onlineUsers.keySet().forEach(a -> temp.append(" ").append(a));
        return temp.toString().trim();
    }


}
