package ufrn.client.interceptors;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.digest.DigestUtils;

import ufrn.client.ClientMarshaller;
import ufrn.client.annotations.MessageInterceptor;
import ufrn.configuration.MiddlewareProperties;
import ufrn.utils.ArrayUtils;

/** Intercepta requisições de conexão para incluir informações de credenciais. */
@MessageInterceptor
public class ClientAuthenticationInterceptor implements Interceptor {

	@Override
	public byte[] before(byte[] mqttMessage) {
		//Verifica se é uma mensagem de conexão e insere as credenciais na mensagem
		if (mqttMessage[0] == ClientMarshaller.MESSAGE_TYPE_CONNECTION) {
			
			String userName = MiddlewareProperties.BROKER_USER.getValue();
			String password = MiddlewareProperties.BROKER_PASSWORD.getValue();
			
			String userNameEncrypted = new DigestUtils("SHA3-256").digestAsHex(userName);
			String passwordEncrypted = new DigestUtils("SHA3-256").digestAsHex(password);
			
			//Codificação do username
			byte[] usernameBytes = userNameEncrypted.getBytes(StandardCharsets.UTF_8);
		    
		    //Codificação do password
			byte[] passwordBytes = passwordEncrypted.getBytes(StandardCharsets.UTF_8);
			
			//tamanho (em bytes) do username
			mqttMessage = ArrayUtils.concatenate(mqttMessage, ClientMarshaller.convertIntTo2Bytes(usernameBytes.length)); 
		    
		    //username
			mqttMessage = ArrayUtils.concatenate(mqttMessage, usernameBytes);
		    
		    //tamanho (em bytes) do password
			mqttMessage = ArrayUtils.concatenate(mqttMessage, ClientMarshaller.convertIntTo2Bytes(passwordBytes.length)); 
		    
		    //password
			mqttMessage = ArrayUtils.concatenate(mqttMessage, passwordBytes);
		}
		
		return mqttMessage;
	}

	@Override
	public byte[] after(byte[] mqttMessage) {
		//Não é necessária nenhuma ação
		return mqttMessage;
	}
	
}
