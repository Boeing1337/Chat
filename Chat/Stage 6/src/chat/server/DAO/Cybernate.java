package chat.server.DAO;

import chat.server.util.DialogStats;
import chat.server.util.UserInfo;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Cybernate {

    private final String infSuffix = ".inf";
    private final String chatSuffix = ".chat";

    private final Set<String> allUsers;

    public Cybernate(final Set<String> allUsers) {
        this.allUsers = allUsers;
        Arrays.stream(Objects.requireNonNull(new File("").listFiles()))
        .forEach(e -> allUsers.add(e.getName()));
    }

    public void createUser(final String login, final String pass, final String rights) {
        try {
            final File directory = new File(login);
            directory.mkdir();
            final File userInfo = new File(login + "\\" + login + infSuffix);
            userInfo.createNewFile();
            try (FileWriter fileWriter = new FileWriter(userInfo)) {
                fileWriter.write(login + " " + pass + " " + rights + " " + "-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        allUsers.add(login);
    }

    public void changeUsersInfo(final String login, final String content) {

    }

    public void saveAsReadMessage(final String owner, final String toUser,
                                  final String message) {

        try (FileWriter fileWriter =
             new FileWriter(new File(owner + "\\" + toUser + chatSuffix))) {

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveAsUnreadMessage(final String owner, final String addressee,
                                    final String message) {

    }

    public String getLastMessages(final String owner, final String user) {
        try (
        Scanner text = new Scanner(new File(owner + "\\" + user + chatSuffix));
        Scanner unread = new Scanner(new File(owner + "\\" + user + infSuffix))) {
            final DialogStats dialogStats = new DialogStats(unread.nextLine().split("\\h"));

            final List<String> temp = new ArrayList<>(50);
            final StringBuilder stringBuilder = new StringBuilder();
            while (text.hasNextLine()) {
                temp.add(text.nextLine());
            }

            final int totalCount = 10 + dialogStats.getUnread();
            for (int i = temp.size() - totalCount; i < temp.size(); i++) {
                if (i < 0)
                    continue;
                stringBuilder.append("\n").append(temp.get(i));
            }
            return stringBuilder.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getHistory(final String owner, final String fromUser,
                             final int count) {
        return null;
    }

    public UserInfo getUserInfo(final String user) {
        UserInfo userInfo = null;
        try (final Scanner scanner = new Scanner(new File(
        user + "\\" + user + infSuffix))) {

            userInfo = new UserInfo(scanner.nextLine().split("\\h"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public void createConversation(final String userA, final String userB) {
        createConversationFiles(userA, userB);
        createConversationFiles(userB, userA);
    }

    private void createConversationFiles(final String owner, final String fromUser) {
        try {
            final File textData = new File(owner + "\\" + fromUser + chatSuffix);
            if (!textData.exists())
                textData.createNewFile();

            final File textStats = new File(owner + "\\" + fromUser + infSuffix);
            if (!textStats.exists())
                textStats.createNewFile();

            try (FileWriter fileWriter = new FileWriter(textStats)) {
                fileWriter.write("0 0 0 0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
