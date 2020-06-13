package chat.server.commands;

import chat.server.UserThread;
import chat.server.commands.interfaces.Command;
import chat.server.util.Message;

public class StatsCommand implements Command {
    private final UserThread.State possibleState = UserThread.State.CONVERSATION;

    @Override
    public void execute(Message m, UserThread ut) {
        if (ut.getState() == possibleState)
            chat.getStats(ut, ut.getAddressee());
    }
}
