package chat.server.util;

public class UserInfo {
    private final String login;
    private final String password;
    private final int rights;
    private final long banTime;

    public UserInfo(final String[] arg) {

        this.login = arg[0];
        this.password = arg[1];
        this.rights = Integer.parseInt(arg[2]);
        this.banTime = Long.parseLong(arg[3]);
    }

    public long getBanTime() {
        return banTime;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getRights() {
        return rights;
    }
}
