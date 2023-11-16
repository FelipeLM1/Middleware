package ufrn.client.exceptions;

/**
 * Representa uma exceção que ocorreu no Broker.
 *  */
public class BrokerException extends Exception {

	private static final long serialVersionUID = -8676659003437502352L;
	
	private String message;

	public BrokerException(String message) {
		super();
		this.message = message;
	}
	
	@Override
		public String getMessage() {
			return message;
		}
	
}
