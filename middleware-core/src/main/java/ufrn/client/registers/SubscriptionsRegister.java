package ufrn.client.registers;

import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import ufrn.client.annotations.Subscribe;

/**
 * Busca por anotações {@link Subscribe} registradas na aplicação.
 *  */
public class SubscriptionsRegister {
	
	public static Set<Method> getSubscriptionMethods(String packageName){
		Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(Scanners.MethodsAnnotated));
		return reflections.getMethodsAnnotatedWith(Subscribe.class);
	}
	
}
