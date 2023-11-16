package ufrn.broker.entities;

import java.util.List;

/** Representa uma publicação em um tópico. Também armazena os IDs dos nós que receberam a publicação. */
public class Publication {

	private String topic;
	private String payload;
	private List<String> receivers;
	
	public Publication() {
		
	}
	
	public Publication(String payload, List<String> receivers) {
		this.payload = payload;
		this.receivers = receivers;
	}

	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public List<String> getReceivers() {
		return receivers;
	}
	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
