package ufrn.broker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import ufrn.broker.entities.MessageHeader;
import ufrn.utils.ArrayUtils;


/**
 * Classe responsável por realizar a serialização e desserialização de informações. 
 */
public class BrokerMarshaller {
	
	public static final int MESSAGE_TYPE_CONNECTION = 0x10;
	public static final int MESSAGE_TYPE_DISCONNECTION = 0xE0;
	public static final int MESSAGE_TYPE_PUBLISH = 0x30;
	public static final int MESSAGE_TYPE_SUBSCRIBE = 0x80;
	public static final int MESSAGE_TYPE_SUBSCRIBE_2 = -0x80;
	public static final int MESSAGE_TYPE_LOOKUP = 0x20;
	public static final int MESSAGE_TYPE_ERROR = 0x00;
	
	/** Converte um número inteiro em bytes  */
	private static byte[] convertIntTo2Bytes(int value) {
		ByteBuffer b = ByteBuffer.allocate(2);
		b.order(ByteOrder.BIG_ENDIAN);
		b.putShort((short) value);

		return b.array();
	}
	
	public static byte[] setToByteArray(Set<String> set) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);

		try {
		    oos.writeObject(set);
		} finally {
		    oos.close();
		}

		return baos.toByteArray();
	}
	
	@SuppressWarnings("unchecked")
	public static Set<String> byteArrayToSet(byte[] bytes) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Set<String> set = null;

		try {
		    set = (Set<String>) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
		    ois.close();
		}
		
		return set;
	}
	
	/** Cria uma mensagem MQTT do tipo <i>look up</i>. 
	 * @throws IOException */
	public static byte[] createMqttLookUpAnswer(Set<String> topics) throws IOException {
		byte[] topicsBytes = setToByteArray(topics);
		byte[] contentLengthBytes = convertIntTo2Bytes(topicsBytes.length);
		
		//Cria o cabeçalho MQTT para uma mensagem de publicação
	    Integer messageType = MESSAGE_TYPE_LOOKUP;
	    Integer flags = 0x00;  //Flags: tamanho (em bytes) do restante da mensagem
	    
	    //Constrói a mensagem MQTT completa
	    byte[] headerBytes = new byte[4];
	    headerBytes[0] = messageType.byteValue(); //tipo da mensagem
	    headerBytes[1] = flags.byteValue(); //flags
	    headerBytes[2] = contentLengthBytes[0];
	    headerBytes[3] = contentLengthBytes[1];
	    
	    byte[] bytes = ArrayUtils.concatenate(headerBytes, topicsBytes);
	    
	    return bytes;
	}

	/** Cria uma mensagem MQTT do tipo <i>connect</i>. */
	public static byte[] createMqttConnectMessage(String identificacao, String username, String password) {
	    //Cria o cabeçalho MQTT para uma mensagem de conexão
		Integer messageType = MESSAGE_TYPE_CONNECTION;  //Tipo de Mensagem: conexao (0x10)
	    Integer flags = 0x00; //Flags: Sem flags definidas

	    //Codificação da identificacao
	    byte[] identificacaoBytes = identificacao.getBytes(StandardCharsets.UTF_8);
		//String identificacaoEncoded = new String(identificacaoBytes, StandardCharsets.UTF_8);
	    
	    //Codificação do username
		byte[] usernameBytes = username.getBytes(StandardCharsets.UTF_8);
		//String usernameEncoded = new String(usernameBytes, StandardCharsets.UTF_8);
	    
	    //Codificação do password
		byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
		//String passwordEncoded = new String(passwordBytes, StandardCharsets.UTF_8);
	    
	    //Constrói a mensagem MQTT completa
	    byte[] messageBytes = new byte[2];
		
	    messageBytes[0] = messageType.byteValue(); //tipo da mensagem
	    messageBytes[1] = flags.byteValue(); //flags
	    
	    //tamanho (em bytes) da identificação
	    messageBytes = ArrayUtils.concatenate(messageBytes, convertIntTo2Bytes(identificacaoBytes.length)); 
	    
	    //identificação
	    messageBytes = ArrayUtils.concatenate(messageBytes, identificacaoBytes);
	    
	    //tamanho (em bytes) do username
	    messageBytes = ArrayUtils.concatenate(messageBytes, convertIntTo2Bytes(usernameBytes.length)); 
	    
	    //username
	    messageBytes = ArrayUtils.concatenate(messageBytes, usernameBytes);
	    
	    //tamanho (em bytes) do password
	    messageBytes = ArrayUtils.concatenate(messageBytes, convertIntTo2Bytes(passwordBytes.length)); 
	    
	    //password
	    messageBytes = ArrayUtils.concatenate(messageBytes, passwordBytes);
	    
	    return messageBytes;

	}
	
	/** Cria uma mensagem MQTT do tipo <i>subscribe</i>. */
	public static byte[] createMqttSubscribeMessage(String topic) {
	    //Cria o cabeçalho MQTT para uma mensagem de inscrição
	    Integer messageType = MESSAGE_TYPE_SUBSCRIBE;  //Tipo de Mensagem: inscrição (0x80)
	    Integer flags = 0x00; //Flags: Sem flags definidas

	    //Codificação do tópico
	    byte[] topicBytes = topic.getBytes(StandardCharsets.UTF_8);
	    
	    //Constrói a mensagem MQTT completa
	    byte[] messageBytes = new byte[2];
		
	    messageBytes[0] = messageType.byteValue(); //tipo da mensagem
	    messageBytes[1] = flags.byteValue(); //flags
	    
	    //tamanho (em bytes) do tópico
	    messageBytes = ArrayUtils.concatenate(messageBytes, convertIntTo2Bytes(topicBytes.length)); 
	    
	    //identificação
	    messageBytes = ArrayUtils.concatenate(messageBytes, topicBytes);

	    return messageBytes;
	}
	
	/** Cria uma mensagem MQTT do tipo <i>publish</i>. */
	public static byte[] createMqttPublishMessage(String topic, String payload) {
	    //Cria o cabeçalho MQTT para uma mensagem de publicação
	    Integer messageType = MESSAGE_TYPE_PUBLISH;
	    Integer flags = 0x00;  //Flags: Sem flags definidas
	    
	    byte[] topicBytes = topic.getBytes(StandardCharsets.UTF_8);
		//String encodedTopic = new String(topicBytes, StandardCharsets.UTF_8);
		
		byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);
		//String encodedPayload = new String(payloadBytes, StandardCharsets.UTF_8);
		
	    //Constrói a mensagem MQTT completa
	    byte[] messageBytes = new byte[2];
	    messageBytes[0] = messageType.byteValue(); //tipo da mensagem
	    messageBytes[1] = flags.byteValue(); //flags
	    
	    //tamanho (em bytes) do tópico
	    messageBytes = ArrayUtils.concatenate(messageBytes, convertIntTo2Bytes(topicBytes.length)); 
	    
	    //tópico
	    messageBytes = ArrayUtils.concatenate(messageBytes, topicBytes);
	    
	    //tamanho (em bytes) do payload
	    messageBytes = ArrayUtils.concatenate(messageBytes, convertIntTo2Bytes(payloadBytes.length));
	    
	    //payload
	    messageBytes = ArrayUtils.concatenate(messageBytes, payloadBytes);
	    
	    return messageBytes;
	}
	
	/** Cria uma mensagem MQTT do tipo <i>error</i>. */
	public static byte[] createMqttErrorMessage(Exception e) {
	    //Cria o cabeçalho MQTT para uma mensagem de publicação
	    Integer messageType = MESSAGE_TYPE_ERROR;
	    Integer flags = 0x00;  //Flags: Sem flags definidas
	    
	    String mensagemErro = "Ocorreu um erro no servidor. Exceção: " + e.getClass().getSimpleName() + ". ";
	    mensagemErro += "Mensagem detalhada: " + e.getMessage();
	    
	    byte[] mensagemErroBytes = mensagemErro.getBytes(StandardCharsets.UTF_8);
		
	    //Constrói a mensagem MQTT completa
	    byte[] messageBytes = new byte[2];
	    messageBytes[0] = messageType.byteValue(); //tipo da mensagem
	    messageBytes[1] = flags.byteValue(); //flags
	    
	    //tamanho (em bytes) da mensagem de erro
	    messageBytes = ArrayUtils.concatenate(messageBytes, convertIntTo2Bytes(mensagemErroBytes.length)); 
	    
	    //mensagem em si
	    messageBytes = ArrayUtils.concatenate(messageBytes, mensagemErroBytes);
	    
	    return messageBytes;
	}
	
	/** Obtém um inteiro a partir de 2 bytes. */
	public static int getIntFrom2BytesArray(byte[] array) {
		int val = ((array[0] & 0xff) << 8) | (array[1] & 0xff);
		return val;
	}
	
	/** Obtém uma String a partir de uma quantidade informada de bytes. */
	public static String getStringFromBytes(byte[] array) {
		return new String(array, StandardCharsets.UTF_8);
	}
	
	/** Traduz as informações presentes nos bytes de um cabeçalho de uma mensagem. */
	public static MessageHeader getHeaderFromBytes(byte[] headerBytes) {
		MessageHeader header = new MessageHeader();
		header.setMessageType(headerBytes[0]); 
        header.setFlags(headerBytes[1]);
        
        return header; 
	}
	
	/** Traduz as informações presentes nos bytes de um cabeçalho de uma mensagem. */
	public static int getMessageTypeFromBytes(byte[] headerBytes) {
        return headerBytes[0]; 
	}
	
}
