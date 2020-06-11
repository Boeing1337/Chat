package chat.server.commands;

import chat.server.UserThread;
import chat.server.commands.interfaces.Command;
import chat.server.util.Message;

public class KickCommand implements Command {
    /**
     * only if ->
     * UserThread.State.ONLINE;
     * UserThread.State.CONVERSATION;
     */

    private final int requiredRights = 2;

    @Override
    public void execute(Message m, UserThread ut) {
        if (ut.getState() != UserThread.State.OFFLINE && ut.getRights() <= requiredRights)
            chat.kick(ut,m.getTarget());

    }
}
