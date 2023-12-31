package ufrn.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for reading properties from the "application.properties" file.
 *
 * <p>The {@code ApplicationPropertiesReader} class provides a simple way to
 * read configuration properties from the "application.properties" file. It uses
 * a static block to load the properties file during class initialization.
 *
 * <p>Usage:
 * <pre>
 * String value = ApplicationPropertiesReader.getProperty("propertyKey");
 * </pre>
 *
 * @see Properties
 */
public class ApplicationPropertiesReader {

    private static final Properties properties = new Properties();
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(ApplicationPropertiesReader.class);

        try {
			properties.load(new FileInputStream("application.properties"));
		} catch (IOException e) {
            logger.info("Não foi encontrado arquivo de configuração no local padrão!");
		}

        if (properties == null || properties.isEmpty()) {
	        try (InputStream input = new FileInputStream("ApplicationBanco/src/main/resources/application.properties")) {
	            properties.load(input);
	        } catch (IOException e) {
	            logger.error(e.getMessage());
	        }
        }
    }

    public static void setPathFileConfiguration(String pathFile){
        if (!pathFile.isEmpty()){
            try (InputStream input = new FileInputStream(pathFile)) {
                properties.load(input);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
