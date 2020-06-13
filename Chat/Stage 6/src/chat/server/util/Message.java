package chat.server.util;

public class Message {
    private final static String exit = "/exit";
    private final static String reg = "/registration ";
    private final static String auth = "/auth ";
    private final static String list = "/list";
    private final static String chat = "/chat ";
    private final static String kick = "/kick ";
    private final static String grant = "/grant ";
    private final static String revoke = "/revoke ";
    private final static String unread = "/unread";
    private final static String history = "/history ";
    private final static String stats = "/stats";
    private final String message;
    private String command = "";
    private String login = "";
    private String pass = "";
    private String target = "";
    private String count = "";

    public Message(final String message) {
        this.message = message;
        process();
    }

    private void process() {
        if (message.startsWith(reg)) {
            command = "registration";
            userInfoParse();
        } else if (message.startsWith(auth)) {
            command = "auth";
            userInfoParse();
        } else if (message.equals(list)) {
            command = "list";
        } else if (message.startsWith(chat)) {
            command = "chat";
            parseWithTarget();
        } else if (message.equals(exit)) {
            command = "exit";
        } else if (message.startsWith(kick)) {
            command = "kick";
            parseWithTarget();
        } else if (message.startsWith(grant)) {
            command = "grant";
            parseWithTarget();
        } else if (message.startsWith(revoke)) {
            command = "revoke";
            parseWithTarget();
        } else if (message.equals(unread)) {
            command = "unread";
        } else if (message.equals(stats)) {
            command = "stats";
        } else if (message.startsWith(history)) {
            command = "history";
            numericCommandParse();
        } else {
            command = "send";
        }
    }

    private void numericCommandParse() {
        String[] words = message.split("\\h");
        if (words.length == 2) {
            count = words[1];
        } else {
            setError();
        }
    }

    private void parseWithTarget() {
        String[] words = message.split("\\h");
        if (words.length == 2) {
            target = words[1];
        } else {
            setError();
        }
    }

    private void userInfoParse() {
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

    public String getCount() {
        return count;
    }
}
