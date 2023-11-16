package ufrn.broker.processors;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import ufrn.broker.BrokerMarshaller;
import ufrn.broker.entities.BasicCommEntities;
import ufrn.broker.entities.Message;
import ufrn.broker.entities.MessageHeader;
import ufrn.broker.entities.Publication;

/** Processa mensagens de subscrição. */
public class SubscribeProcessor extends BrokerMessageProcessor {

	public SubscribeProcessor(BasicCommEntities comm, List<String> subscribedTopics,
			Map<String, List<Publication>> topicPublications) {
		super(comm, subscribedTopics, topicPublications);
	}

	@Override
	protected Message lerMensagem(MessageHeader cabecalho) throws IOException {
		Message m = new Message();
		m.setCabecalho(cabecalho);
		
		//Lê a quantidade de bytes do tópico (codificação de comprimento variável)
    	int topicLength = BrokerMarshaller.getIntFrom2BytesArray(input.readNBytes(2));
    	
    	//Lê o tópico
    	String topic = BrokerMarshaller.getStringFromBytes(input.readNBytes(topicLength));
    	
    	m.setPublication(new Publication());
    	m.getPublication().setTopic(topic);
		
        return m;
	}
	
	@Override
	protected void processar(Message m) {
		System.out.println("Subscrição recebida...");
		
		subscribedTopics.add(m.getPublication().getTopic());
	}

	@Override
	public int getMessagetype() {
		return BrokerMarshaller.MESSAGE_TYPE_SUBSCRIBE_2;
	}

}
