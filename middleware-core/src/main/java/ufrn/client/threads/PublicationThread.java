package ufrn.client.threads;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import ufrn.client.Requestor;

/** Thread responsável por realizar publicações em tópicos. */
public class PublicationThread extends Thread {
	
	/** Métodos registrados que deverão ser chamados para se realizar uma publicação. */
	private Set<Method> publishMethods;
	
	private int publishInterval;
	
	private Requestor requestor;
	
	public PublicationThread(Requestor requestor, int publishInterval, Set<Method> methods) {
		super();
		this.requestor = requestor;
		this.publishInterval = publishInterval;
		this.publishMethods = methods;
	}

	@Override
	public void run() {
		try {
			boolean continuar = true;
			
			while (continuar) {
				
				for (Method m : publishMethods) {
					// métodos de publicação retornam false caso as publicações tenham se encerrado 
					continuar = (boolean) m.invoke(null, requestor);
			    }
		        
		        Thread.sleep(publishInterval);
			} 
			
			System.out.println("Encerrando publicações...");
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InterruptedException e) {
			System.out.println("Erro... Interrompendo publicações.");
			e.printStackTrace();
		} 
	}

}
