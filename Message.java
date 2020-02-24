import java.io.Serializable;

public class Message implements Serializable{
	// Allows this class to be sent as a series of bytes
	
	private String messageText;
	private String senderName;
	
	public Message(String messageText, String senderName) {
		this.messageText = messageText;
		this.senderName = senderName;
	}
	
	public String toString() {
		return this.senderName + ": " + this.messageText;
	}
}
