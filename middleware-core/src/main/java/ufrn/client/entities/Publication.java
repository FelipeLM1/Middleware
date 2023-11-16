package ufrn.client.entities;

/**
 * Representa um dado que foi publicado. Armazena o tópico e o valor 
 * publicado.
 *  */
public class Publication {

	private String topico;
	private String valor;
	
	public Publication(String topico, String valor) {
		super();
		this.topico = topico;
		this.valor = valor;
	}
	
	public String getTopico() {
		return topico;
	}
	public void setTopico(String topico) {
		this.topico = topico;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
}
