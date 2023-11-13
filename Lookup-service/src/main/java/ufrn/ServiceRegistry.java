package ufrn;

import java.util.HashSet;
import java.util.Set;

public class ServiceRegistry {

    private static final Set<ServiceDto> httpServices = new HashSet<>();
    private static final Set<ServiceDto> mqttServices = new HashSet<>();

    public static void addHttpService(ServiceDto serviceDto) {
        httpServices.add(serviceDto);
    }

    public static void addMqttService(ServiceDto serviceDto) {
        mqttServices.add(serviceDto);
    }


}
