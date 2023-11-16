package ufrn.client.registers;

import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import ufrn.client.annotations.Publish;

/**
 * Busca por anotações {@link Publish} registradas na aplicação.
 *  */
public class PublishMethodsRegister {
	
	public static Set<Method> getPublishMethods(String packageName){
		Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(Scanners.MethodsAnnotated));
		return reflections.getMethodsAnnotatedWith(Publish.class);
	}
	
}
