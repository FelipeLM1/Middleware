package ufrn.broker.entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Armazena as entidades básicas para comunicação (Socket, DataInputStream, DataOutputStream. 
*/
public class BasicCommEntities {

	private final DataInputStream input;
	private final DataOutputStream output;
	private final Socket socket;
	
	public BasicCommEntities(DataInputStream input, DataOutputStream output, Socket socket) {
		this.input = input;
		this.output = output;
		this.socket = socket;
	}
	
	public DataInputStream getInput() {
		return input;
	}
	public DataOutputStream getOutput() {
		return output;
	}
	public Socket getSocket() {
		return socket;
	}
	
	
}
