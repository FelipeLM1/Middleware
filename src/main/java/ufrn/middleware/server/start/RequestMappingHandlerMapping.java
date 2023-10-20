package ufrn.middleware.server.start;

import ufrn.middleware.server.broker.ObjectId;

public class RequestMappingHandlerMapping {
    public static void init() {
        registerMethods();
    }


    private static void registerMethods() {
        searchHttpMethods();
    }

    private static void searchHttpMethods() {
        ScannerRequestMethods.scanAndAddMethods("ufrn.app.controller");
        ObjectId.printRegisteredMethods();

    }
}
