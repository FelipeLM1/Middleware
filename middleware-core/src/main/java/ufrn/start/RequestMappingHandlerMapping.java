package ufrn.start;

import ufrn.configuration.AcquisitionType;
import ufrn.configuration.ApplicationPropertiesReader;
import ufrn.configuration.MiddlewareProperties;
import ufrn.methods.staticLifecycle.ObjectIdStatic;

/**
 * A class responsible for initializing and managing the request mapping for the Middleware application.
 *
 * <p>The `RequestMappingHandlerMapping` class is responsible for initializing and configuring request mapping
 * for the Middleware application. It invokes the `ScannerRequestMethods.scanAndAddMethods()` method to scan
 * for annotated methods in the specified package and registers them for handling HTTP methods. It also prints
 * the registered methods using `ObjectId.printRegisteredMethods()`.
 *
 * @see ScannerEagerRequestMethods
 * @see ObjectIdStatic
 */
public class RequestMappingHandlerMapping {

    private RequestMappingHandlerMapping() {
        throw new IllegalStateException("Utility class");
    }

    public static void init() {
        registerMethods();
    }


    private static void registerMethods() {
        searchHttpMethods();
    }


    private static void searchHttpMethods() {
        if (MiddlewareProperties.ACQUISITION_TYPE.getValue().equals(AcquisitionType.EAGER.name())) {
            ScannerEagerRequestMethods.scanAndAddMethods(ApplicationPropertiesReader.getProperty(MiddlewareProperties.SCAN_PACKAGE.getPropertyKey()));
            ObjectIdStatic.printRegisteredMethods();
        }

    }
}
