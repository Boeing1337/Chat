package chat.server.commands;

import chat.server.UserThread;
import chat.server.commands.interfaces.Command;
import chat.server.util.Message;

public class AuthCommand implements Command {

    private final UserThread.State possibleState = UserThread.State.OFFLINE;

    @Override
    public void execute(final Message m, final UserThread ut) {
        if (ut.getState() == possibleState)
            chat.auth(ut, m.getLogin(), m.getPass());
    }
}
