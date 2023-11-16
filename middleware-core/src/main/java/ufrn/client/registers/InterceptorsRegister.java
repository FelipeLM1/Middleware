package ufrn.client.registers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import ufrn.client.annotations.MessageInterceptor;
import ufrn.client.interceptors.Interceptor;
import ufrn.configuration.MiddlewareProperties;

/**
 * Busca por {@link Interceptor} registrados na aplicação.
 *  */
public class InterceptorsRegister {
	
	/** Lista de interceptadores de mensagens a serem enviadas. */
	private static List<Interceptor> interceptors = null;

	public static Set<Class<?>> getRegisteredInterceptors() {
		String middlewarePackage = MiddlewareProperties.MIDDLEWARE_PACKAGE_NAME.getValue();
		Reflections reflections = new Reflections(middlewarePackage);
		return reflections.getTypesAnnotatedWith(MessageInterceptor.class);
	}
	
	/** Registra os Interceptors do sistema. */
	public static void registerInterceptors() {
		Set<Class<?>> classes = getRegisteredInterceptors();
		
		interceptors = new ArrayList<>();
		
		try {
			if (classes != null) {
				
				for (Class<?> c : classes) {
					Interceptor i = (Interceptor) c.getDeclaredConstructor().newInstance();
					interceptors.add(i);
				}
				
			} 
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Interceptor> getInterceptors(){
		if (interceptors == null)
			registerInterceptors();
		
		return interceptors;
	}
	
}
