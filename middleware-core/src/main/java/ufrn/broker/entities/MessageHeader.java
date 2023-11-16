package ufrn.broker.entities;

/** 
 * Representa o cabeçalho de uma mensagem.
 * */
public class MessageHeader {

	private int messageType;
	
	private int flags;

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}
	
}
