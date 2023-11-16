package ufrn.broker.registers;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import ufrn.broker.entities.BasicCommEntities;
import ufrn.broker.entities.Publication;
import ufrn.broker.processors.BrokerMessageProcessor;

public class ProcessorsRegister {
	
	/** Lista de interceptadores de mensagens a serem enviadas. */
	private static Set<Class<? extends BrokerMessageProcessor>> processors = null;
	
	private static void registerProcessors() {
		processors = new HashSet<>();
		
		Reflections reflections = new Reflections("ufrn.broker.processors");
		processors.addAll(reflections.getSubTypesOf(BrokerMessageProcessor.class));
	}
	
	/** Busca a implementação adequada do BrokerMessageProcessor, baseado no tipo de mensagem. */
	public static BrokerMessageProcessor getCorrectInstance(int messageType, BasicCommEntities comm, 
			List<String> subscribedTopics,
			Map<String, List<Publication>> topicPublications) {
		
		if (processors == null)
			registerProcessors();
		
		try {
			if (processors != null) {
				for (Class<? extends BrokerMessageProcessor> c : processors) {
					
					BrokerMessageProcessor p = 
							c.getConstructor(BasicCommEntities.class, List.class, Map.class)
								.newInstance(comm, subscribedTopics, topicPublications);
					
					if (p.getMessagetype() == messageType)
						return p;
				}
			} 
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
