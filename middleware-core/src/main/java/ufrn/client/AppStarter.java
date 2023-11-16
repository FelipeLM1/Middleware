package ufrn.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import ufrn.client.exceptions.BrokerException;
import ufrn.client.registers.PublishMethodsRegister;
import ufrn.client.registers.SubscriptionsRegister;
import ufrn.client.threads.PublicationThread;
import ufrn.configuration.MiddlewareProperties;

public class AppStarter {
	
	/** Controla as requisições feitas ao servidor (<i>broker</i>). */
	private static Requestor requestor = null;
	
	public static void start() {
		try {
			System.out.println("Conectando-se...");
			requestor = new Requestor(MiddlewareProperties.IDENTIFICACAO.getValue());
			
			registerSubscriptions();
			
			performPublications();
			
			receivePublications();
			
		} catch (Exception e) {
			System.out.println("Erro! Desconectando...");
	        e.printStackTrace();
			
	        if (requestor != null)
				requestor.encerrar();
		}
	}

	/** Cria uma thread para ficar fazendo publicações. */
	private static void performPublications() {
		//Busca por métodos com anotação @Publish no pacote da aplicação
		Set<Method> methods = PublishMethodsRegister.getPublishMethods(
				MiddlewareProperties.APP_PACKAGE_NAME.getValue());
		
		//Se houver métodos de publicação
		if (methods != null && !methods.isEmpty()) {
			int publishInterval = Integer.parseInt(MiddlewareProperties.PUBLISH_INTERVAL.getValue());
			
			//Inicia uma thread para realizar as publicações periodicamente
			PublicationThread t = new PublicationThread(requestor, publishInterval, methods);
			t.start();
		}
	}

	/** Recebe publicações indefinidamente. */
	private static void receivePublications() 
				throws IOException, BrokerException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		while (true) {
			requestor.receivePublication();
		}
	}

	/**
	 * Procura por métodos registrados para subscrição no sistema e realiza a invocação
	 * desses métodos.
	 */
	private static void registerSubscriptions() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//Procurando por métodos com anotação @Subscribe
		Set<Method> subscribeMethods = SubscriptionsRegister.getSubscriptionMethods(
				MiddlewareProperties.APP_PACKAGE_NAME.getValue());
		
		if (subscribeMethods != null && !subscribeMethods.isEmpty()) {
			System.out.println("Registrando subscrições...");
			
			for (Method m : subscribeMethods) {
		        m.invoke(null, requestor); 
		    }
		}
	}
	
}
