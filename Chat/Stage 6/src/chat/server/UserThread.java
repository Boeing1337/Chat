package chat.server;

import chat.server.commands.interfaces.Command;
import chat.server.util.Message;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class UserThread implements Runnable {
    private final Map<String, Command> commands;
    private final IOManager ioManager;
    private State state = State.OFFLINE;
    private String login = "";
    private String addressee = "";
    private int rights;

    public UserThread(final IOManager ioManager, final Map<String, Command> commands) {
        this.ioManager = ioManager;
        this.commands = commands;
    }

    @Override
    public void run() {
        sentTechnicalMessage("auth or register");
        while (!ioManager.isSocketClosed()) {
            final Message message = new Message(ioManager.read());
            final Command command = commands.get(message.getCommand());
            if (command != null)
                command.execute(message, this);
        }

    }

    public void stop() {
        ioManager.closeSocket();
    }

    public void sentMessage(@NotNull final String message) {
        ioManager.sent(message);
    }

    public void sentTechnicalMessage(@NotNull final String message) {
        ioManager.sent("Server: " + message);
    }

    public State getState() {
        return state;
    }

    public void setState(@NotNull final State state) {
        this.state = state;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull final String login) {
        this.login = login;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(@NotNull final String addressee) {
        this.addressee = addressee;
    }

    public int getRights() {
        return rights;
    }

    public void setRights(final int rights) {
        this.rights = rights;
    }

    public enum State {
        OFFLINE,
        ONLINE,
        CONVERSATION
    }
}


