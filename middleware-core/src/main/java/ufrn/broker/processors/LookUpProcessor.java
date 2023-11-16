package ufrn.broker.processors;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ufrn.broker.BrokerMarshaller;
import ufrn.broker.entities.BasicCommEntities;
import ufrn.broker.entities.Message;
import ufrn.broker.entities.MessageHeader;
import ufrn.broker.entities.Publication;

/** Processa mensagens de subscribe. */
public class LookUpProcessor extends BrokerMessageProcessor {
	
	public LookUpProcessor(BasicCommEntities comm, List<String> subscribedTopics,
			Map<String, List<Publication>> topicPublications) {
		super(comm, subscribedTopics, topicPublications);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Message lerMensagem(MessageHeader cabecalho) throws IOException {
		Message m = new Message();
		m.setCabecalho(cabecalho);
		
        return m;
	}
	
	@Override
	protected void processar(Message m) throws IOException {
		transmitirTopicosDisponiveis();
	}

	/** Transmite os tópicos disponíveis quando um <i>look up</i> é solicitado. 
	 * @throws IOException */
	private void transmitirTopicosDisponiveis() throws IOException {
		
    	Set<String> topicsCopy = null;
    	
    	/* 
    	 * Cria uma cópia dos tópicos atualmente disponíveis. A operação de look up é 
    	 * realizada com uma cópia para que o trecho de código sincronizado não se torne 
    	 * muito grande e consequentemente custoso.
    	*/ 
    	synchronized (this) {
    		topicsCopy = Set.copyOf(topicPublications.keySet());
    	}
    	
    	byte[] messageBytes = BrokerMarshaller.createMqttLookUpAnswer(topicsCopy);
    	enviarMensagem(messageBytes);
    	
    	System.out.println("Solicitação de tópicos disponíveis recebida e respondida");
		
	}
	
	/** Envia uma mensagem para o cliente, na forma de bytes. 
	 * @throws IOException */
	private void enviarMensagem(byte[] mqttMessage) throws IOException {
	    output.write(mqttMessage); //escreve a mensagem em si
	}
	
	@Override
	public int getMessagetype() {
		return BrokerMarshaller.MESSAGE_TYPE_LOOKUP;
	}
	
}
