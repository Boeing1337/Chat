package chat.server.commands;

import chat.server.UserThread;
import chat.server.commands.interfaces.Command;
import chat.server.util.Message;

public class SendCommand implements Command {
    private final UserThread.State possibleState = UserThread.State.CONVERSATION;

    @Override
    public void execute(Message m, UserThread ut) {
        final UserThread.State state = ut.getState();
        if (state == UserThread.State.OFFLINE)
            ut.sentTechnicalMessage(" you are not in the chat!");
        else if (state == UserThread.State.ONLINE)
            ut.sentTechnicalMessage(" use /list command to choose an user to text!");
        else if (state == possibleState) {
            final String login = ut.getLogin();
            final String message = login + ": " + m.getMessage();
            chat.sentMessage(login, ut.getAddressee(), message);
        }
    }
}
