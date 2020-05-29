package chat.server;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ChatData {
    private final Map<String, UserThread> onlineUsers = new HashMap<>();
    private final Map<String, String> allUsers = new HashMap<>();
    private final Set<String> conversations = new HashSet<>();

    synchronized boolean registry(final UserThread userThread, final String login,
                                  final String pass) {

        if (allUsers.get(login) != null) {
            userThread.sentMessage("this login is already in use!");
            return false;
        }
        if (pass.length() < 8) {
            userThread.sentMessage("the password is too short!");
            return false;
        }

        allUsers.put(login, pass);
        onlineUsers.put(login, userThread);
        userThread.sentMessage("you are registered successfully!");
        try {
            Files.createDirectories(Paths.get(login));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    synchronized boolean auth(final UserThread userThread, final String login,
                              final String pass) {

        final String tempPass = allUsers.get(login);

        if (tempPass == null) {
            userThread.sentMessage("incorrect login!");
            return false;
        }
        if (!tempPass.equals(pass)) {
            userThread.sentMessage("incorrect password!");
            return false;
        }

        userThread.sentMessage("you are authorized successfully!");
        onlineUsers.put(login, userThread);
        return true;
    }

    synchronized void sentMessage(final String fromUser, final String toUser,
                                  final String message) {
        if (conversations.contains(fromUser + toUser))
            onlineUsers.get(toUser).sentMessage(fromUser + ": " + message);

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
        } catch (Exception r) {
            r.printStackTrace();
        }
    }

    private boolean isFileExist(final String fromUser, final String toUser) {
        return new File(toUser + "/" + fromUser).exists();
    }

    synchronized String getLastMessages(final String fromUser, final String toUser) {
        if (!isFileExist(fromUser, toUser))
            return "";
        File file = new File(toUser + "/" + fromUser);
        try (Scanner scanner = new Scanner(file)) {
            ArrayList<String> temp = new ArrayList<>();
            while (scanner.hasNextLine()) {
                temp.add(scanner.nextLine());
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = temp.size() - 1; i >= 0; i--) {
                stringBuilder.append("\n").append(temp.get(i));
            }
            return stringBuilder.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    synchronized void logOut(final String userName) {
        onlineUsers.remove(userName);
    }

    synchronized void creatConversation(final String fromUser, final String toUser) {
        System.out.println(fromUser + ", " + toUser);
        conversations.add(fromUser + toUser);
    }

    synchronized void removeConversation(final String fromUser, final String toUser) {
        conversations.remove(fromUser + toUser);
    }

    boolean isAddresseeOnline(final String target) {
        return onlineUsers.get(target) != null;
    }

    String getOnlineUsers(final String owner) {
        final StringBuilder temp = new StringBuilder();
        temp.append("online:");
        Set<String> set = new HashSet<>(onlineUsers.keySet());
        set.remove(owner);
        set.forEach(a -> temp.append(" ").append(a));
        return temp.toString().trim();
    }


}
