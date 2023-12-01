package ufrn.client.registers;

import java.util.Set;

import org.reflections.Reflections;

import ufrn.client.annotations.Subscribe;

/**
 * Busca por anotações {@link Subscribe} registradas na aplicação.
 *  */
public class SubscriptionsRegister {
	
	public static Set<Class<?>> getSubscriptionClasses(String packageName){
		Reflections reflections = new Reflections(packageName);
		return reflections.getTypesAnnotatedWith(Subscribe.class);
	}
	
}
