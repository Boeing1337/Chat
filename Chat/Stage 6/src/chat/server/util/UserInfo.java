package chat.server.util;

public class UserInfo {
    private final String login;
    private final String password;
    private int rights;
    private long banTime;

    public UserInfo(final String rawString) {
        final String[] arg = rawString.split("\\h");
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

    public void grant() {
        this.rights = 2;
    }

    public void revoke() {
        this.rights = 3;
    }

    @Override
    public String toString() {
        return login + " " + password + " " + rights + " " + banTime;
    }
}
