package chat.server.util;

public class DialogStats {
    private int count;
    private int owner;
    private int fromUser;
    private int unread;

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

    @Override
    public String toString() {
        return count + " " + owner + " " + fromUser + " " + unread;
    }

    public void addUnread() {
        unread++;
        count++;
    }

    public void addRead() {
Ебаня ИДея всё похерила .ывАываыяваплджбывакпяолд.вапряолдж.
    }
}
