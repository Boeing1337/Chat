package chat.server.commands.fabrics;

import chat.server.commands.*;
import chat.server.commands.interfaces.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandsConfigurator {

    public static Map<String, Command> getCommandsMap() {
        final HashMap<String, Command> commands = new HashMap<>();
        commands.put("registration", new RegistrationCommand());
        commands.put("auth", new AuthCommand());
        commands.put("send", new SendCommand());
        commands.put("exit", new ExitCommand());
        commands.put("chat", new ChatCommand());
        commands.put("kick", new KickCommand());
        commands.put("list", new ListCommand());
        commands.put("grant", new GrantCommand());
        commands.put("revoke", new RevokeCommand());
        commands.put("unread", new UnreadCommand());
        commands.put("stats", new StatsCommand());
        commands.put("history", new HistoryCommand());
        return commands;
    }
}
