package ufrn.middleware.start;

import ufrn.middleware.methods.ObjectId;

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
