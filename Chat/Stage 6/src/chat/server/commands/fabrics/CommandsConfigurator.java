package chat.server.commands.fabrics;

import chat.server.commands.AuthCommand;
import chat.server.commands.RegistrationCommand;
import chat.server.commands.interfaces.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandsConfigurator {

    public static Map<String, Command> getCommandsMap() {
        final HashMap<String, Command> commands = new HashMap<>();
        commands.put("registration", new RegistrationCommand());
        commands.put("auth", new AuthCommand());
        return commands;
    }
}
