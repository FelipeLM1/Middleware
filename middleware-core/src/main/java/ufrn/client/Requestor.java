package ufrn.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ufrn.client.entities.MessageHeader;
import ufrn.client.entities.Publication;
import ufrn.client.exceptions.BrokerException;
import ufrn.client.interceptors.Interceptor;
import ufrn.client.registers.InterceptorsRegister;
import ufrn.client.registers.ReceiveMethodsRegister;

/** 
 * Responsável pela comunicação do cliente com o servidor.
 * Utilizado para separar a implementação da comunicação da lógica de negócio. 
 * */
public class Requestor {

	/** Lista de interceptadores de mensagens a serem enviadas. */
	private List<Interceptor> interceptors;
	
	/** Gerencia as comunicações via rede. */
	private ClientRequestHandler clientHandler;
	
	/**
	 * Construtor padrão do requisitor.
	 * 
	 * @param identificacao Identificação do nó cliente que está se conectando com o <i>broker</i>.
	 * @throws IOException 
	 */
	public Requestor(String identificacao) {
		interceptors = new ArrayList<>();
		interceptors.addAll(InterceptorsRegister.getInterceptors());
		
		try {
			conectar(identificacao);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * 	Conecta-se ao <i>broker</i>.
	 * 
	 *  @param identificacaoNo Nome de identificação do nó que está se conectando.
	 *  
	 * @throws IOException 
	 *  */
	private void conectar(String identificacaoNo) throws IOException {
		clientHandler = new ClientRequestHandler();
		clientHandler.conectar(identificacaoNo);
		
		byte[] mqttMessage = ClientMarshaller.createMqttConnectMessage(identificacaoNo);
		enviarMensagem(mqttMessage);
	}
	
	/** Encerra a comunicação do cliente com o servidor.
	 * @throws IOException */
	public void encerrar() {
		clientHandler.encerrar();
	}
	
	/** 
	 * Realiza uma subscrição em um tópico de interesse da aplicação.
	 * 
	 * @param topic Tópico no qual será feita a subscrição.
	 * @param cls Nome da classe onde está o método que será chamado ao receber uma publicação
	 * no tópico.
	 * @param callBackMethodName Nome do método que será chamado ao receber publicações
	 * no método subscrito.
	 *  */
	public void subscribe(String topic, Class<?> cls, String callBackMethodName) throws IOException {
	    ReceiveMethodsRegister.addClassMethodToTopic(topic, cls, callBackMethodName);
		
		byte[] mqtt_message = ClientMarshaller.createMqttSubscribeMessage(topic);
	    enviarMensagem(mqtt_message);
	}
	
	/** Bloqueia a execução da <i>thread</i> até o recebimento da próxima publicação. */
	public void receivePublication() 
			throws IOException, BrokerException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	    
		//Aguarda o recebimento do próximo cabeçalho (2 bytes)
        byte[] headerBytes = clientHandler.lerNBytes(2);
	    MessageHeader header = ClientMarshaller.getHeaderFromBytes(headerBytes);
        
        //se for mensagem de publicação
        if (header.getMessageType() == ClientMarshaller.MESSAGE_TYPE_PUBLISH) {
        	int topicLength = ClientMarshaller.getIntFrom2BytesArray(clientHandler.lerNBytes(2));
            String topic = ClientMarshaller.getStringFromBytes(clientHandler.lerNBytes(topicLength));
            
            int payloadLength = ClientMarshaller.getIntFrom2BytesArray(clientHandler.lerNBytes(2));
            String pl = ClientMarshaller.getStringFromBytes(clientHandler.lerNBytes(payloadLength));
            
            Publication p = new Publication(topic, pl);
            ReceiveMethodsRegister.callTopicMethods(topic, this, p);
        
        } else if (header.getMessageType() == ClientMarshaller.MESSAGE_TYPE_ERROR) {
        	int messageLength = ClientMarshaller.getIntFrom2BytesArray(clientHandler.lerNBytes(2));
            String message = ClientMarshaller.getStringFromBytes(clientHandler.lerNBytes(messageLength));
        	
            throw new BrokerException(message);
        }
		
	}
	
	/** Procura por tópicos disponíveis para inscrição no servidor. 
	 * @throws IOException */
	public Set<String> lookUpTopics() throws IOException{
		
		//Cria a mensagem de lookup
	    byte[] mqttMessage = ClientMarshaller.createMqttLookUpMessage();
	    enviarMensagem(mqttMessage);
	    
	    //Aguarda a resposta com os tópicos disponíveis
	    //Lê o próximo cabeçalho
	    byte[] bytes = clientHandler.lerNBytes(2);
	    MessageHeader header = ClientMarshaller.getHeaderFromBytes(bytes);
        
        //se for mensagem de publicação
        if (header.getMessageType() == ClientMarshaller.MESSAGE_TYPE_LOOKUP) {
        	byte[] lengthBytes = clientHandler.lerNBytes(2);
        	int length = ClientMarshaller.getIntFrom2BytesArray(lengthBytes);
        	
        	byte[] topicsBytes = clientHandler.lerNBytes(length);
        	Set<String> topics = ClientMarshaller.byteArrayToSet(topicsBytes);
        	return topics;
        }
		
	    return null;
	}
	
	/**
	 * Envia uma mensagem, dando oportunidade aos Interceptors registrados
	 * de realizarem ações prévias e posteriores. 
	 *  */
	private void enviarMensagem(byte[] mqttMessage) throws IOException {
	    
		if (interceptors != null) {
			for (Interceptor i : interceptors) {
				mqttMessage = i.before(mqttMessage);
			}
		}
		
		clientHandler.escreverMensagem(mqttMessage);
		
		if (interceptors != null) {
			for (Interceptor i : interceptors) {
				mqttMessage = i.after(mqttMessage);
			}
		}
	}
	
	/** Publica um conteúdo novo em um tópico. */
	public void publicar(String topico, String payload, boolean log) throws IOException {
	    //Cria a mensagem a ser publicada
	    byte[] mqttMessage = ClientMarshaller.createMqttPublishMessage(topico, payload);
	    enviarMensagem(mqttMessage);
	    
	    if (log) {
		    System.out.println("Publicando..."); 
		    System.out.println("Topico: " + topico);    
		    System.out.println("Valor: " + payload);
		    System.out.println("");
	    }
	}
	
}
