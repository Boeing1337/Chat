package chat.server.commands;

import chat.server.UserThread;
import chat.server.commands.interfaces.Command;
import chat.server.util.Message;

public class ExitCommand implements Command {

    @Override
    public void execute(Message m, UserThread ut) {
        chat.logOut(ut.getLogin(), ut.getAddressee());
        ut.sentTechnicalMessage("this session has been finished!");
        ut.stop();
    }
}
