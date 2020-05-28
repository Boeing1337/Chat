package chat.server.util;

public class Message {
    private final static String exit = "/exit";
    private final static String reg = "/registration ";
    private final static String auth = "/auth ";
    private final static String list = "/list";
    private final static String chat = "/chat ";
    private final String message;
    private String command = "";
    private String login = "";
    private String pass = "";
    private String target = "";

    public Message(final String message) {
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
        } else if (message.startsWith(chat)) {
            command = "chat";
            parseChat();
        } else if (message.equals(exit)) {
            command = "exit";
        } else {
            command = "text";
        }
    }

    private void parseChat() {
        String[] words = message.split("\\h");
        if (words.length == 2) {
            target = words[1];
        } else {
            setError();
        }
    }

    private void parse() {
        String[] words = message.split("\\h");
        if (words.length == 3) {
            login = words[1];
            pass = words[2];
        } else {
            setError();
        }
    }

    private void setError() {
        command = "error";
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

    public String getTarget() {
        return target;
    }
}
