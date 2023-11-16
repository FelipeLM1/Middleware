package ufrn.exceptions;

public class CredenciaisInvalidasException extends Exception {

	private static final long serialVersionUID = -8777490352511778980L;
	
	@Override
	public String getMessage() {
		System.out.println("Credenciais inválidas! Cliente NÃO conectado.");
		return "Credenciais inválidas! Cliente NÃO conectado.";
	}
	
}
