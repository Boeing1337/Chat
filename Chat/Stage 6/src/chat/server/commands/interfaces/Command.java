package chat.server.commands.interfaces;

import chat.server.Chat;
import chat.server.UserThread;
import chat.server.util.Message;

public interface Command {
    Chat chat = new Chat();

    void execute(final Message m, final UserThread ut);
}
