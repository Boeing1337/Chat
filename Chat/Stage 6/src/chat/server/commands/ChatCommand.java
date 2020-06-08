package chat.server.commands;

import chat.server.UserThread;
import chat.server.commands.interfaces.Command;
import chat.server.util.Message;

public class ChatCommand implements Command {
    /**
     * UserThread.State.ONLINE;
     * UserThread.State.CONVERSATION;
     */
    @Override
    public void execute(Message m, UserThread ut) {
        if (ut.getState() != UserThread.State.OFFLINE)
            chat.setConversation(ut, m.getTarget());

    }
}
