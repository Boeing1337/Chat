package chat.server.util;

public class UserInfo {
    private final String login;
    private final String password;
    private final String rights;


    public UserInfo(final String login, final String password, final String rights) {
        this.login = login;
        this.password = password;
        this.rights = rights;
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
