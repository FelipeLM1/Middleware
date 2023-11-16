package ufrn.broker.processors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ufrn.broker.BrokerMarshaller;
import ufrn.broker.entities.BasicCommEntities;
import ufrn.broker.entities.Message;
import ufrn.broker.entities.MessageHeader;
import ufrn.broker.entities.Publication;

/** Processa mensagens de publicação. */
public class PublishProcessor extends BrokerMessageProcessor {

	public PublishProcessor(BasicCommEntities comm, List<String> subscribedTopics,
			Map<String, List<Publication>> topicPublications) {
		super(comm, subscribedTopics, topicPublications);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Message lerMensagem(MessageHeader cabecalho) throws IOException {
		Message m = new Message();
		m.setCabecalho(cabecalho);
		m.setPublication(new Publication());
    	
    	int topicLength = BrokerMarshaller.getIntFrom2BytesArray(input.readNBytes(2));
    	m.getPublication().setTopic(BrokerMarshaller.getStringFromBytes(input.readNBytes(topicLength)));
        
    	int payloadLength = BrokerMarshaller.getIntFrom2BytesArray(input.readNBytes(2));
    	m.getPublication().setPayload(BrokerMarshaller.getStringFromBytes(input.readNBytes(payloadLength)));  
		
        return m;
	}
	
	@Override
	protected void processar(Message m) {
		String topic = m.getPublication().getTopic();
    	String payload = m.getPublication().getPayload();
    	
    	synchronized (this) {
        	if (topicPublications.get(topic) == null)
        		topicPublications.put(topic, new ArrayList<>());
        	
        	Publication pub = new Publication();
        	pub.setPayload(payload);
        	pub.setReceivers(new ArrayList<>());
        	
        	List<Publication> publications = topicPublications.get(topic);
        	publications.add(pub);
    	}
    	
    	System.out.println("\n(" + m.getPublication().getTopic() + ") "
        		+ "RECEBIDO valor " + m.getPublication().getPayload());
    	
	}

	@Override
	public int getMessagetype() {
		return BrokerMarshaller.MESSAGE_TYPE_PUBLISH;
	}

}
