package chat.server.util;

public class DialogStats {
    private int count;
    private int owner;
    private int fromUser;
    private int unread;

    public DialogStats(final String rawString) {
        final String[] arg = rawString.split("\\h");
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

    @Override
    public String toString() {
        return count + " " + owner + " " + fromUser + " " + unread;
    }

    public void increaseRead(final String owner, final String fromUser) {
        if (owner.equals(fromUser)) {
            this.owner++;
        } else {
            this.fromUser++;
        }

        this.count++;

    }

    public void increaseUnread() {
        this.count++;
        this.unread++;
        this.fromUser++;
    }

    public void clearUnread() {
        this.unread = 0;
    }
}
