package server;
import java.sql.Timestamp;

public class Message {
    private String sender;
    private String recipient;

    private Timestamp timeMark;
    private String text;

    public Message() {
        timeMark = new Timestamp(System.currentTimeMillis());
    }

    public Message(String sender, String message){
        this.sender=sender;
        this.text=message;
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTimeMark() {
        return timeMark;
    }

    public void setTimeMark(Timestamp timeMark) {
        this.timeMark = timeMark;
    }
}
