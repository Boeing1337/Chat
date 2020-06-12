package chat.server.DAO;

import chat.server.util.DialogStats;
import chat.server.util.UserInfo;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Cybernate {

    private final Set<String> allUsers;

    public Cybernate(final Set<String> allUsers) {
        this.allUsers = allUsers;
        Arrays.stream(Objects.requireNonNull(new File(".").listFiles()))
        .forEach(e -> allUsers.add(e.getName()));
    }

    public void createUser(final String login, final String pass, final String rights) {
        try {
            final File directory = new File(login);
            directory.mkdir();
            final File userInfo = formInfFile(login, login);
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

    public void saveAsReadMessage(final String owner, final String fromUser,
                                  final String message) {

        try (FileWriter fileWriter = new FileWriter(formChatFile(owner, fromUser), true)) {
            fileWriter.write(message + "\n");
            addReadCount(owner, fromUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAsUnreadMessage(final String owner, final String fromUser,
                                    final String message) {
        try (FileWriter fileWriter = new FileWriter(formChatFile(owner, fromUser), true)) {
            fileWriter.write(message + "\n");
            addUnreadCount(owner, fromUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addReadCount(final String owner, final String fromUser) {
        final File file = formInfFile(owner, fromUser);
        try (Scanner stats = new Scanner(file)) {
            DialogStats dialogStats = new DialogStats(stats.nextLine().split("\\h"));
            dialogStats.increaseRead(owner, fromUser);
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(dialogStats.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addUnreadCount(final String owner, final String fromUser) {
        final File file = formInfFile(owner, fromUser);
        try (Scanner stats = new Scanner(file)) {
            DialogStats dialogStats = new DialogStats(stats.nextLine().split("\\h"));
            dialogStats.increaseUnread();
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(dialogStats.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLastMessages(final String owner, final String fromUser) {
        try (
        Scanner textScanner = new Scanner(formChatFile(owner, fromUser));
        Scanner statsScanner = new Scanner(formInfFile(owner, fromUser))) {
            final String stats = statsScanner.nextLine();
            final DialogStats dialogStats = new DialogStats(stats.split("\\h"));
            final List<String> temp = new ArrayList<>(50);
            final StringBuilder stringBuilder = new StringBuilder();
            while (textScanner.hasNextLine()) {
                temp.add(textScanner.nextLine());
            }

            final int totalCount = 10 + dialogStats.getUnread();
            for (int i = temp.size() - totalCount; i < temp.size(); i++) {
                if (i < 0)
                    continue;
                stringBuilder.append("\n").append(temp.get(i));
            }
            dialogStats.clearUnread();
            try (FileWriter fileWriter = new FileWriter(formInfFile(owner, fromUser))) {
                fileWriter.write(dialogStats.toString());
            }
            return stringBuilder.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private String readLine(final File file) {
        try (Scanner scanner = new Scanner(file)) {
            return scanner.nextLine();
        } catch (Exception ignored) {
        }
        return "";
    }

    private List<String> realAllLines(final File file) {
        try (Scanner scanner = new Scanner(file)) {
            final List<String> tempList = new ArrayList<>(50);
            while (scanner.hasNextLine()) {
                tempList.add(scanner.nextLine());
            }
            return tempList;
        } catch (Exception ignored) {
        }
        return new ArrayList<>();
    }

    public String getHistory(final String owner, final String fromUser,
                             final int count) {
        return null;
    }

    public UserInfo getUserInfo(final String user) {
        UserInfo userInfo = null;
        try (final Scanner scanner = new Scanner(formInfFile(user, user))) {
            userInfo = new UserInfo(scanner.nextLine().split("\\h"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public void saveUserInfo(final String user, final UserInfo userInfo) {
        try (FileWriter fileWriter = new FileWriter(formInfFile(user, user))) {
            fileWriter.write(userInfo.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createConversation(final String userA, final String userB) {
        createConversationFiles(userA, userB);
        createConversationFiles(userB, userA);
    }

    private void createConversationFiles(final String owner, final String fromUser) {
        try {
            final File textData = formChatFile(owner, fromUser);
            final File textStats = formInfFile(owner, fromUser);
            if (!textData.exists() && !textStats.exists()) {
                textData.createNewFile();
                textStats.createNewFile();
                try (FileWriter fileWriter = new FileWriter(textStats)) {
                    fileWriter.write("0 0 0 0");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private File formChatFile(final String a, final String b) {
        final String chatSuffix = ".chat";
        return new File(a + "\\" + b + chatSuffix);
    }

    private File formInfFile(final String a, final String b) {
        final String infSuffix = ".inf";
        return new File(a + "\\" + b + infSuffix);
    }
}
