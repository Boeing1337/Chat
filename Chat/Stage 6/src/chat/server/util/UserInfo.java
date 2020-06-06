package chat.server.util;

public class UserInfo {
    private final String login;
    private final String password;
    private final String rights;
    private final long ubanTime;


    public UserInfo(final String login, final String password, final String rights,
                    final long unbanTime) {
        this.login = login;
        this.password = password;
        this.rights = rights;
        this.ubanTime = unbanTime;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRights() {
        return rights;
    }
}
