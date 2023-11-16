package ufrn.client.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Classes que desejam interceptar mensagens devem possuir esta anotação. */
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageInterceptor  {

}
