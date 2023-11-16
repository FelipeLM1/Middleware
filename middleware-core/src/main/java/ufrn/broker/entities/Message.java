package ufrn.broker.entities;

/** Representa uma mensagem recebida de um cliente. */
public class Message {

	private MessageHeader cabecalho;
	private String identificacaoNo;
	private String username;
	private String password;
	private Publication publication;
	
	public MessageHeader getCabecalho() {
		return cabecalho;
	}
	public void setCabecalho(MessageHeader cabecalho) {
		this.cabecalho = cabecalho;
	}
	public String getIdentificacaoNo() {
		return identificacaoNo;
	}
	public void setIdentificacaoNo(String identificacaoNo) {
		this.identificacaoNo = identificacaoNo;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Publication getPublication() {
		return publication;
	}
	public void setPublication(Publication publication) {
		this.publication = publication;
	}
}
