package ufrn.client.interceptors;

/** 
 * Um Interceptor tem a oportunidade de interceptar o envio de mensagens com o
 * intuito de realizar operações antes e/ou após seu envio.
 *  */
public interface Interceptor {

	/** Ação que será executada antes do envio de uma mensagem. */
	public byte[] preEnvio(byte[] mqttMessage);
	
	/** Ação que será executada após o envio de uma mensagem. */
	public byte[] posEnvio(byte[] mqttMessage);
	
}
