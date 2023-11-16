package ufrn.broker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ufrn.broker.entities.BasicCommEntities;
import ufrn.broker.entities.Publication;
import ufrn.configuration.MiddlewareProperties;

/** 
 * Gerenciador de comunicação do servidor com os clientes.
 * Recebe requisições e estabelece conexões para atendê-las.
 * */
public class BrokerRequestHandler {
	
	/** 
	 * Map que armazena as publicações de cada tópico e quem as recebeu. 
	 * Chave: tópico.
	 * Valores: publicações do tópico e quem já as recebeu. 
	 * */
	private static Map<String, List<Publication>> topicPublications = new HashMap<>();
	
	public static void main(String[] args) throws IOException {
		iniciarServidorMQTT();
	}

	public static void iniciarServidorMQTT() throws IOException {
		String host = MiddlewareProperties.BROKER_HOST.getValue();
		int port = Integer.parseInt(MiddlewareProperties.BROKER_PORT.getValue());
		ServerSocket brokerSocket = new ServerSocket(port);
		
		System.out.println("=========================================");
		System.out.println("::Servidor MQTT iniciado em " + host + ":" + port);
		System.out.println("=========================================\n");
		
		//obtendo requisições de clientes
		while (true) {
			Socket newSocket = null;
			
			try {
				//aguardando recebimento de requisições de clientes
				newSocket = brokerSocket.accept();
				
				System.out.println(":: Conexão aceita: " + newSocket + " ::");
				
				//obtendo streams de input e output
				DataInputStream dataInputstream = new DataInputStream(newSocket.getInputStream());
				DataOutputStream dataOutputstream = new DataOutputStream(newSocket.getOutputStream());
				
				BasicCommEntities comm = new BasicCommEntities(dataInputstream, dataOutputstream, newSocket);
				
				BrokerInvoker t = new BrokerInvoker(comm, topicPublications);
				t.start();
				
			} catch (Exception e){
				newSocket.close();
				e.printStackTrace();
			}
		}
	}
	
	
	
}
