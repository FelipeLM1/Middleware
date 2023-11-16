package ufrn.broker;

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
import ufrn.broker.processors.BrokerMessageProcessor;
import ufrn.broker.registers.ProcessorsRegister;
import ufrn.broker.util.SendMessageUtils;

/**
 * Gerencia uma conexão com um cliente. <br/><br/>
 * Recebe requisições desse cliente e invoca o objeto remoto adequado.
*/
public class BrokerInvoker extends Thread {
	
	/** Entidades básicas de comunicação. */
	private BasicCommEntities comm;
	
	private final DataInputStream input;
	private final DataOutputStream output;
	private final Socket socket;
	
	/** Identificação do nó com o qual se estabeleceu conexão. */
	private String identificacaoNo = null;
	
	/** Lista de tópicos subscritos pelo cliente conectado. */
	private List<String> subscribedTopics = new ArrayList<>();
	
	/** 
	 * Map que armazena as publicações de cada tópico e quem as recebeu. 
	 * Chave: tópico.
	 * Valores: publicações do tópico e quem já as recebeu. 
	 * */
	private Map<String, List<Publication>> topicPublications;
	
	/** Construtor */
	public BrokerInvoker(BasicCommEntities comm, Map<String, List<Publication>> topicPublications) {
		this.comm = comm;
		
		this.socket = comm.getSocket();
		this.input = comm.getInput();
		this.output = comm.getOutput();
		
		this.topicPublications = topicPublications;
	}

	@Override
	public void run() {

		try {
			
			while (true) {

	            //Se não tiver recebido nenhuma mensagem
	            if (input.available() < 2) {
	            	
	            	//Verifica se tem publicações para transmitir
		            SendMessageUtils.sendPendingPublications(output, identificacaoNo, subscribedTopics, topicPublications);
	            	
		            Thread.sleep(250);
	            
	            } else {
	            	//Recebendo uma mensagem
	            	
		            //Lê o cabeçalho (2 bytes)
		            byte[] headerBytes = input.readNBytes(2); // read the message
	            	
		            MessageHeader cabecalho = BrokerMarshaller.getHeaderFromBytes(headerBytes); //Extrai o tipo de mensagem e as flags
		            
		            /*------------ Identifica o tipo de mensagem e invoca o processador correto ------------*/
		            
		            BrokerMessageProcessor p = ProcessorsRegister.getCorrectInstance(
		            		cabecalho.getMessageType(), 
		            		comm, 
		            		subscribedTopics,
		            		topicPublications);
		            
		            Message m = p.processarMensagem(cabecalho);
		            
		            if (cabecalho.getMessageType() == BrokerMarshaller.MESSAGE_TYPE_CONNECTION) {
		                identificacaoNo = m.getIdentificacaoNo();
		            }
		            
		            if (cabecalho.getMessageType() == BrokerMarshaller.MESSAGE_TYPE_DISCONNECTION) {
		                break;
		            }
		                
		            //Verifica se tem publicações para transmitir
		            SendMessageUtils.sendPendingPublications(output, identificacaoNo, subscribedTopics, topicPublications);
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
			SendMessageUtils.publicarErro(output, e);
		} finally {
			encerrarConexoes();
		}
	}
	
	private void encerrarConexoes() {
		try {
			input.close();
			output.close();
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

}
