package ufrn.broker.processors;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ufrn.broker.entities.BasicCommEntities;
import ufrn.broker.entities.Message;
import ufrn.broker.entities.MessageHeader;
import ufrn.broker.entities.Publication;
import ufrn.broker.interceptors.Interceptor;
import ufrn.broker.registers.InterceptorsRegister;

/**
 * Classe abstrata que representa um processador de mensagem.
 * Cada tipo de mensagem recebida possui dados diferentes
 * e devem ser processadas de maneiras diferentes.
 * Por isso, todo tipo de mensagem existente deve ter seu 
 * próprio processador, que deve estender esta classe. 
 *  */
public abstract class BrokerMessageProcessor {
	
	protected DataInputStream input;
	protected DataOutputStream output;
	protected Socket socket;
	
	/** 
	 * Map que armazena as publicações de cada tópico e quem as recebeu. 
	 * Chave: tópico.
	 * Valores: publicações do tópico e quem já as recebeu. 
	 * */
	protected Map<String, List<Publication>> topicPublications;
	
	/** Lista de tópicos subscritos pelo cliente conectado. */
	protected List<String> subscribedTopics = new ArrayList<>();
	
	/** Lista de interceptadores de mensagens a serem enviadas. */
	private List<Interceptor> interceptors;
	
	public BrokerMessageProcessor(BasicCommEntities comm, 
			List<String> subscribedTopics,
			Map<String, List<Publication>> topicPublications) {
		
		this.input = comm.getInput();
		this.output = comm.getOutput();
		this.socket = comm.getSocket();
		
		this.subscribedTopics = subscribedTopics;
		this.topicPublications = topicPublications;
		
		interceptors = new ArrayList<>();
		interceptors.addAll(InterceptorsRegister.getInterceptors());
	}
	
	/** 
	 * Lê uma mensagem a partir de seus bytes. Os dados presentes
	 * na mensagem dependerão do tipo de mensagem. 
	 */
	protected abstract Message lerMensagem(MessageHeader cabecalho) throws IOException;
	
	/** 
	 * Realiza algum processamento específico na mensagem.
	 * Esse processamento irá depender do tipo de mensagem.
	 */
	protected abstract void processar(Message m) throws Exception;
	
	/** 
	 * Retorna o código do tipo de mensagem pelo qual o processador em questão 
	 * é responsável.
	 */
	public abstract int getMessagetype();

	/**
	 * Método público utilizado para processar uma mensagem.
	 * Realiza o processamento de uma mensagem, chamadando os 
	 * interceptors registrados antes e depois do processamento.  
	 */
	public Message processarMensagem(MessageHeader cabecalho) throws Exception {
		Message m = lerMensagem(cabecalho);
		interceptacoesPrevias(m);
		processar(m);
		interceptacoesPosteriores(m);
		
		return m;
	}
	
	/** Chama os interceptors registrados para realizar um processamento prévio na mensagem. */
	private void interceptacoesPrevias(Message m) throws Exception {
		if (interceptors != null) {
			for (Interceptor i : interceptors) {
				m = i.preRecebimento(m);
			}
		}
	}
	
	/** Chama os interceptors registrados para realizar um processamento posterior na mensagem. */
	private void interceptacoesPosteriores(Message m) throws Exception {
		if (interceptors != null) {
			for (Interceptor i : interceptors) {
				m = i.posRecebimento(m);
			}
		}
	}
	
	protected void encerrarConexoes() {
		try {
			input.close();
			output.close();
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
