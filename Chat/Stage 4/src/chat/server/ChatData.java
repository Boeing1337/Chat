package chat.server;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatData {
    private final List<String> messages = new ArrayList<>();
    private final HashMap<String, UserThread> users = new HashMap<>();

    synchronized boolean registry(@NotNull final UserThread userThread,
                                  @NotNull String userName) {
        userName = userName.trim();
        if (isUserExist(userName))
            return false;

        users.put(userName, userThread);
        return true;
    }

    void unRegistry(@NotNull final String userName) {
        users.remove(userName);
    }

    synchronized void addMessage(@NotNull final String message) {
        messages.add(message);
        users.forEach((a, b) -> b.sentMessage(message));
    }

    private boolean isUserExist(final String user) {
        return users.get(user) != null;
    }

    synchronized String getLastMessages() {
        final StringBuilder string = new StringBuilder();
        for (int i = messages.size() - 1, s = 0; s < 10 && i >= 0; i--, s++) {
            string.append(messages.get(i));
        }
        return string.toString();
    }

}
