package ufrn.broker.interceptors;

import ufrn.broker.entities.Message;

/** 
 * Um Interceptor tem a oportunidade de interceptar o envio de mensagens com o
 * intuito de realizar operações antes e/ou após seu envio.
 *  */
public interface Interceptor {

	/** Ação que será executada antes do envio de uma mensagem. */
	public Message preRecebimento(Message m) throws Exception;
	
	/** Ação que será executada após o envio de uma mensagem. */
	public Message posRecebimento(Message m) throws Exception;
	
}
