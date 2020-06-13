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
            write(userInfo, login + " " + pass + " " + rights + " " + "-1", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        allUsers.add(login);
    }

    public void saveMessage(final String owner, final String fromUser,
                            final String message) {
        write(formChatFile(owner, fromUser), message + "\n", true);
    }

    public void saveConversationInfo(final String owner, final String fromUser,
                                     final DialogStats dialogStats) {
        final File file = formInfFile(owner, fromUser);
        write(file, dialogStats.toString(), false);
    }

    public DialogStats getDialogStats(final String owner, final String fromUser) {
        return new DialogStats(readLine(formInfFile(owner, fromUser)));
    }

    public String getHistory(final String owner, final String fromUser,
                               final int count) {

        final File chatFile = formChatFile(owner, fromUser);
        final List<String> temp = realAllLines(chatFile);
        final StringBuilder stringBuilder = new StringBuilder();

        int maxCount = 1;
        for (int i = temp.size() - count; i < temp.size(); i++, maxCount++) {
            if (i < 0)
                continue;
            stringBuilder.append("\n").append(temp.get(i));
            if (maxCount == 100)
                break;
        }
        return stringBuilder.toString().trim();
    }

    public String getLastMessages(final String owner, final String fromUser) {
        final File infFile = formInfFile(owner, fromUser);
        final File chatFile = formChatFile(owner, fromUser);
        final String rawStats = readLine(infFile);

        final DialogStats dialogStats = new DialogStats(rawStats);
        final List<String> temp = realAllLines(chatFile);
        final StringBuilder stringBuilder = new StringBuilder();

        int totalCount = 10 + dialogStats.getUnread();
        totalCount = Math.min(totalCount, 100);
        int maxCount = 1;
        for (int i = temp.size() - totalCount; i < temp.size(); i++, maxCount++) {
            if (i < 0)
                continue;
            stringBuilder.append("\n").append(temp.get(i));
            if (maxCount == 100)
                break;
        }
        dialogStats.clearUnread();
        write(infFile, dialogStats.toString(), false);
        return stringBuilder.toString().trim();
    }

    private String readLine(final File file) {
        try (Scanner scanner = new Scanner(file)) {
            return scanner.nextLine();
        } catch (Exception ignored) {
        }
        return "";
    }

    private void write(final File file, final String text, final boolean append) {
        try (FileWriter fileWriter = new FileWriter(file, append)) {
            fileWriter.write(text);
        } catch (Exception ignored) {
        }
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

    public UserInfo getUserInfo(final String user) {
        return new UserInfo(readLine(formInfFile(user, user)));
    }

    public void saveUserInfo(final String user, final UserInfo userInfo) {
        write(formInfFile(user, user), userInfo.toString(), false);
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
                write(textStats, "0 0 0 0", false);
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

    public String parseUnread(final String owner) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(Objects.requireNonNull(new File(owner + "\\").listFiles()))
        .filter(e -> e.getName().endsWith(".inf") && !e.getName().startsWith(owner))
        .forEach(file -> {
            final DialogStats dialogStats = new DialogStats(readLine(file));
            final int unread = dialogStats.getUnread();
            if (unread != 0) {
                final String name = file.getName().replaceAll("\\..+", "");
                stringBuilder
                .append(" ").append(name).append("(").append(unread).append(
                ")");
            }
        });
        return stringBuilder.toString().trim();
    }
}
