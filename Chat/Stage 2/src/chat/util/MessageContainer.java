package chat.util;

public class MessageContainer {
    private volatile String message = "";


    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public boolean isEmpty() {
        return message.isEmpty();
    }
}
