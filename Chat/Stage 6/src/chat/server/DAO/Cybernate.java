package chat.server.DAO;

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

    public void saveAsReadMessage(final String owner, final String addressee,
                                  final String message) {

    }

    public void saveAsUnreadMessage(final String owner, final String addressee,
                                    final String message) {

    }

    public String getLastMessages(final String owner, final String user) {
        try (
        Scanner text = new Scanner(new File(owner + "\\" + user + chatSuffix));
        Scanner unread = new Scanner(new File(owner + "\\" + user + infSuffix))) {
            final int count;
            if (unread.hasNextLine())
                count = unread.nextInt();

            final List<String> temp = new ArrayList<>();
            final StringBuilder stringBuilder = new StringBuilder();
            while (text.hasNextLine()) {
                temp.add(text.nextLine());
            }

            for (int i = temp.size() - 10, step = 0; step < 10; i++, step++) {
                if (i < 0)
                    continue;
                stringBuilder.append("\n").append(temp.get(i));
            }
            return stringBuilder.toString().trim();


        } catch (Exception e) {
            e.printStackTrace();
        }
        //10 messages + all unread
    }

    public String getHistory(final String owner, final String fromUser,
                             final int count) {
        //10 messages + all unread
    }

    public UserInfo getUserInfo(final String user) {
        if (allUsers.contains(user)) {

            try (final Scanner scanner = new Scanner(new File(
            user + "\\" + user + infSuffix))) {

                return new UserInfo(scanner.nextLine().split("\\h"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                fileWriter.write(
                "count=0\n" +
                owner + "=0\n" +
                fromUser + "=0\n+" +
                "unread=0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
