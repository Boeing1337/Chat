package chat.server;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class ChatData {
    private final Map<String, UserThread> onlineUsers = new HashMap<>();
    private final Map<String, String> allUsers = new HashMap<>();
    private final Set<String> conversations = new HashSet<>();

    ChatData() {
        File file = new File("allUsers");
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    allUsers.put(scanner.next(), scanner.next());
                }
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized boolean registry(final UserThread userThread, final String login,
                                  final String pass) {

        if (allUsers.get(login) != null) {
            userThread.sentTechnicalMessage("this login is already in use!");
            return false;
        }
        if (pass.length() < 8) {
            userThread.sentTechnicalMessage("the password is too short!");
            return false;
        }

        allUsers.put(login, pass);
        onlineUsers.put(login, userThread);
        try (FileWriter file = new FileWriter("allUsers", true)) {
            file.write(login + " " + pass + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        userThread.sentTechnicalMessage("you are registered successfully!");
        return true;
    }

    synchronized boolean auth(final UserThread userThread, final String login,
                              final String pass) {

        final String tempPass = allUsers.get(login);

        if (tempPass == null) {
            userThread.sentTechnicalMessage("incorrect login!");
            return false;
        }
        if (!tempPass.equals(pass)) {
            userThread.sentTechnicalMessage("incorrect password!");
            return false;
        }

        userThread.sentTechnicalMessage("you are authorized successfully!");
        onlineUsers.put(login, userThread);
        return true;
    }

    synchronized void sentMessage(final String fromUser, final String toUser,
                                  final String message) {

        final File messages = new File(generateFileName(fromUser, toUser));

        if (conversations.contains(toUser + fromUser))
            onlineUsers.get(toUser).sentMessage(fromUser + ": " + message);

        if (!messages.exists()) {
            try {
                messages.createNewFile();
            } catch (Exception ignored) {
            }
        }

        try (FileWriter file = new FileWriter(messages.getName(), true)) {
            file.write(fromUser + ": " + message + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String generateFileName(final String a, final String b) {
        final List<String> strings = new ArrayList<>(List.of(a, b));
        strings.sort(Comparator.naturalOrder());
        return strings.get(0) + strings.get(1);
    }

    synchronized String getLastMessages(final String fromUser, final String account) {
        File file = new File(generateFileName(fromUser, account));

        try (Scanner scanner = new Scanner(file)) {
            final List<String> temp = new ArrayList<>();
            final StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                temp.add(scanner.nextLine());
            }

            for (int i = temp.size() - 10, step = 0; step < 10; i++, step++) {
                if (i < 0)
                    continue;
                stringBuilder.append("\n").append(temp.get(i));
            }
            return stringBuilder.toString().trim();
        } catch (Exception ignored) {
        }
        return "";
    }

    synchronized void logOut(final String userName, final String toUser) {
        conversations.remove(userName + toUser);
        onlineUsers.remove(userName);
    }

    synchronized void setConversation(final String fromUser, final String toUser) {
        conversations.remove(fromUser + toUser);
        conversations.add(fromUser + toUser);
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
