package ufrn.broker.processors;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import ufrn.broker.BrokerMarshaller;
import ufrn.broker.entities.BasicCommEntities;
import ufrn.broker.entities.Message;
import ufrn.broker.entities.MessageHeader;
import ufrn.broker.entities.Publication;

/** Processa mensagens de conexão. */
public class ConnectionProcessor extends BrokerMessageProcessor {


	public ConnectionProcessor(BasicCommEntities comm, List<String> subscribedTopics,
			Map<String, List<Publication>> topicPublications) {
		super(comm, subscribedTopics, topicPublications);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Message lerMensagem(MessageHeader cabecalho) throws IOException {
		Message m = new Message();
		m.setCabecalho(cabecalho);
		
		int identificacaoLength = BrokerMarshaller.getIntFrom2BytesArray(input.readNBytes(2));
        m.setIdentificacaoNo(BrokerMarshaller.getStringFromBytes(input.readNBytes(identificacaoLength)));
        int usernameLength = BrokerMarshaller.getIntFrom2BytesArray(input.readNBytes(2));
        m.setUsername(BrokerMarshaller.getStringFromBytes(input.readNBytes(usernameLength)));   
        int passwordLength = BrokerMarshaller.getIntFrom2BytesArray(input.readNBytes(2));
        m.setPassword(BrokerMarshaller.getStringFromBytes(input.readNBytes(passwordLength)));
		
        return m;
	}
	
	@Override
	protected void processar(Message m) {
		
	}

	@Override
	public int getMessagetype() {
		return BrokerMarshaller.MESSAGE_TYPE_CONNECTION;
	}

}
