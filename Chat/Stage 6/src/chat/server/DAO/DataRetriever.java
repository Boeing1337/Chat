package chat.server.DAO;

import chat.server.util.UserInfo;

import java.util.Set;

public class DataRetriever {

    private final Set<String> allUsers;

    public DataRetriever(final Set<String> allUsers) {
        this.allUsers = allUsers;
    }

    public void createUser(final String login, final String pass, final String rights) {
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

    public String getLastMessages(final String owner, final String fromUser) {
        //10 messages + all unread
    }

    public String getMessages(final String owner, final String fromUser,
                              final int count) {
        //10 messages + all unread
    }

    public UserInfo getUserInfo(final String user) {

    }

    public boolean isUserExist(final String User) {

    }
}
