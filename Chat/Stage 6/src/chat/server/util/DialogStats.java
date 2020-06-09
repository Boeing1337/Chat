package chat.server.util;

public class DialogStats {
    private final int count;
    private final int owner;
    private final int fromUser;
    private final int unread;

    public DialogStats(final String[] arg) {
        this.count = Integer.parseInt(arg[0]);
        this.owner = Integer.parseInt(arg[1]);
        this.fromUser = Integer.parseInt(arg[2]);
        this.unread = Integer.parseInt(arg[3]);
    }


    public int getCount() {
        return count;
    }

    public int getOwner() {
        return owner;
    }

    public int getFromUser() {
        return fromUser;
    }

    public int getUnread() {
        return unread;
    }
}
