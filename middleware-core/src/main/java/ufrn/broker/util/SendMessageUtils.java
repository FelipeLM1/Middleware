package ufrn.broker.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import ufrn.broker.BrokerMarshaller;
import ufrn.broker.entities.Publication;

/** Métodos úteis para envio de mensagens. */
public class SendMessageUtils {

	/** Transmite as publicações subscritas por um nó que estão pendentes de envio. */
	public static void sendPendingPublications(DataOutputStream output, 
			String identificacaoNo, 
			List<String> subscribedTopics, 
			Map<String, List<Publication>> topicPublications) throws IOException {
		
		for (String subscribedTopic : subscribedTopics) {
            List<Publication> publications = topicPublications.get(subscribedTopic); 
            
            if (publications != null) {
		        for (Publication p : publications) {
		            boolean enviado = p.getReceivers().contains(identificacaoNo);
		            
		            if (!enviado) {
		            	publicar(output, subscribedTopic, p.getPayload());
		                p.getReceivers().add(identificacaoNo);
		                System.out.println("\n(" + subscribedTopic + ") TRANSMITIDO valor " + p.getPayload() + 
		                		" para " + identificacaoNo);
		            }
		        }
            }
        }
		
	}
	
	/** Informa ao cliente a ocorrência de um erro no Broker. */
	public static void publicarErro(DataOutputStream output, Exception e) {
	    
	    try {
	    	byte[] mqttMessage = BrokerMarshaller.createMqttErrorMessage(e);
	    	enviarMensagem(output, mqttMessage);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
	}
	
	/** Publica um conteúdo novo em um tópico. */
	private static void publicar(DataOutputStream output, String topico, String payload) throws IOException {
	    //Cria a mensagem a ser publicada
	    byte[] mqttMessage = BrokerMarshaller.createMqttPublishMessage(topico, payload);
	    enviarMensagem(output, mqttMessage);
	}
	
	/** Envia uma mensagem para o cliente, na forma de bytes. 
	 * @throws IOException */
	private static void enviarMensagem(DataOutputStream output, byte[] mqttMessage) throws IOException {
	    output.write(mqttMessage); //escreve a mensagem em si
	}
	
}
