package ufrn.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import ufrn.configuration.MiddlewareProperties;


/** 
 * Gerencia comunicações via rede.
 * Responsável por abrir e fechar conexões com o servidor, enviar requisições, 
 * receber respostas e despachá-las para o REQUESTOR apropriado.
 */
public class ClientRequestHandler {

	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	
	public ClientRequestHandler() {
		
	} 
	
	/** 
	 * 	Conecta-se ao <i>broker</i>.
	 * 
	 *  @param identificacaoNo Nome de identificação do nó que está se conectando.
	 *  
	 * @throws IOException 
	 *  */
	public void conectar(String identificacaoNo) throws IOException {
		String brokerHost = MiddlewareProperties.BROKER_HOST.getValue();
		int brokerPort = Integer.parseInt(MiddlewareProperties.BROKER_PORT.getValue());
		
		//estabelecendo conexão
		InetAddress inetAddress = InetAddress.getByName(brokerHost);
		socket = new Socket(inetAddress, brokerPort);
		
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
	}
	
	/** Fecha socket e <i>streams</i>. 
	 * @throws IOException */
	public void encerrar() {
		try {
			socket.close();
		    input.close();
			output.close();
		} catch (IOException e) {
			System.out.println("Falha ao encerrar comunicação...");
			e.printStackTrace();
		}
	}
	
	/** Envia uma mensagem para o <i>Broker</i>, na forma de bytes. 
	 * @throws IOException */
	public void escreverMensagem(byte[] mqttMessage) throws IOException {
	    output.write(mqttMessage); //envia a mensagem em si
	}
	
	/** 
	 * Ler a quantidade de bytes especificada. Bloqueia a <i>thread</i> até o
	 * recebimento da quantidade total dos bytes. 
	 * */
	public byte[] lerNBytes(int qtd) throws IOException {
		return input.readNBytes(qtd);
	}
	
	/** 
	 * Ler todos os bytes restantes da mensagem. Bloqueia a <i>thread</i> até o
	 * recebimento da quantidade total dos bytes. 
	 * */
	public byte[] lerTodosOsBytes() throws IOException {
		return input.readAllBytes();
	}
	
}
