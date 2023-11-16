package ufrn.client.registers;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import ufrn.client.Requestor;
import ufrn.client.entities.ClassMethod;
import ufrn.client.entities.Publication;

/**
 * Armazena os métodos que devem ser chamados ao se receber uma
 * publicação.
 *  */
public class ReceiveMethodsRegister {
	
	/** 
	 * Mapa com tópicos e nomes de métodos que devem ser chamados ao se receber publicações 
	 * no tópico em questão. <br/><br/>
	 * Chave: nome do tópico.<br/>
	 * Valores: classe e nome do método que deverá ser chamado ao se receber publicações
	 * no tópico em questão.
	 *  */
	private static Map<String, ClassMethod> registeredTopics = new HashMap<>();
	
	/** Adiciona um novo método que deverá ser chamado ao se receber publicações em um tópico. */
	public static void addClassMethodToTopic(String topic, Class<?> cls, String methodName) {
		ClassMethod cm = new ClassMethod();
		cm.setClazz(cls);
		cm.setMethodName(methodName);
		
		registeredTopics.put(topic, cm);
	}

	/** Retorna o método que deve ser chamado após o recebimento do tópico em questão. */
	public static ClassMethod getMethodFromTopic(String topic) {
		return registeredTopics.get(topic);
	}
	
	/** Chama os métodos registrados para o tópico em questão. */
	public static void callTopicMethods(String topic, Requestor requestor, Publication p) 
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		ClassMethod cm = getMethodFromTopic(topic);
		
		cm.getClazz().getDeclaredMethod(cm.getMethodName(), Requestor.class, Publication.class)
			.invoke(null, requestor, p);
		
	}
}
