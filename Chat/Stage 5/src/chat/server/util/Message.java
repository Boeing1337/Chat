package chat.server.util;

import org.jetbrains.annotations.NotNull;

public class Message {
    private final static String reg = "/registration ";
    private final static String auth = "/auth ";
    private final static String list = "/list";
    private final String message;
    private String command = "";
    private String login = "";
    private String pass = "";

    public Message(@NotNull final String message) {
        this.message = message;
        process();
    }

    private void process() {
        if (message.startsWith(reg)) {
            command = "registration";
            parse();
        } else if (message.startsWith(auth)) {
            command = "auth";
            parse();
        } else if (message.equals(list)) {
            command = "list";
        } else {
            text();
        }
    }

    private void text() {
        command = "text";
    }

    private void parse() {
        String[] words = message.split("\\h");
        if (words.length == 3) {
            login = words[1];
            pass = words[2];
        }
    }


    public String getPass() {
        return pass;
    }

    public String getCommand() {
        return command;
    }

    public String getLogin() {
        return login;
    }

    public String getMessage() {
        return message;
    }
}
