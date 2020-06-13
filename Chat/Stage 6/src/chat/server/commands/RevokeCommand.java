package chat.server.commands;

import chat.server.UserThread;
import chat.server.commands.interfaces.Command;
import chat.server.util.Message;

public class RevokeCommand implements Command {
    /**
     * only if ->
     * UserThread.State.ONLINE;
     * UserThread.State.CONVERSATION;
     */

    private final int rights = 1;

    @Override
    public void execute(Message m, UserThread ut) {
        if (ut.getRights() == rights && ut.getState() != UserThread.State.OFFLINE)
            chat.revoke(m.getTarget());
    }
}
