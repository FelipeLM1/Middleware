package ufrn.broker.interceptors;

import org.apache.commons.codec.digest.DigestUtils;

import ufrn.broker.BrokerMarshaller;
import ufrn.broker.annotations.MessageInterceptor;
import ufrn.broker.entities.Message;
import ufrn.configuration.MiddlewareProperties;
import ufrn.exceptions.CredenciaisInvalidasException;

/** Intercepta requisições de conexão para incluir informações de credenciais. */
@MessageInterceptor
public class BrokerAuthenticationInterceptor implements Interceptor {

	@Override
	public Message preRecebimento(Message m) throws CredenciaisInvalidasException {
		//Verifica se é uma mensagem de conexão e insere as credenciais na mensagem
		
		if (m.getCabecalho().getMessageType() == BrokerMarshaller.MESSAGE_TYPE_CONNECTION) {
			String username = MiddlewareProperties.BROKER_USER.getValue();
			String password = MiddlewareProperties.BROKER_PASSWORD.getValue();
			
			String userNameEncrypted = new DigestUtils("SHA3-256").digestAsHex(username);
			String passwordEncrypted = new DigestUtils("SHA3-256").digestAsHex(password);
			
			if (m.getUsername().equals(userNameEncrypted) && m.getPassword().equals(passwordEncrypted) ){
	            System.out.println("Cliente " + m.getIdentificacaoNo() + " autenticado!");
	        } else {
	        	throw new CredenciaisInvalidasException();
	        }
		}
		
		return m;
	}

	@Override
	public Message posRecebimento(Message m) {
		//Não é necessária nenhuma ação
		return m;
	}
	
}
