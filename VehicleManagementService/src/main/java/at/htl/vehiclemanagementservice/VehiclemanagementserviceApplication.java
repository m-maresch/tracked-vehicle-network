package at.htl.vehiclemanagementservice;

import at.htl.vehiclemanagementservice.handler.UserHandler;
import at.htl.vehiclemanagementservice.handler.VehicleActionHandler;
import at.htl.vehiclemanagementservice.handler.VehicleHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class VehiclemanagementserviceApplication {

    private final String contextPath = "/api";

    @Bean
    RouterFunction<ServerResponse> composedRoutes(UserHandler userHandler, VehicleHandler vehicleHandler, VehicleActionHandler vehicleActionHandler) {
        return userRoutes(userHandler)
                .and(vehicleRoutes(vehicleHandler))
                .and(vehicleActionRoutes(vehicleActionHandler));
    }

    private RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return
                route(POST(contextPath + "/login"), handler::login);
    }

    private RouterFunction<ServerResponse> vehicleRoutes(VehicleHandler handler) {
        return
                route(GET(contextPath + "/vehicles"), handler::getAll)
                        .and(route(GET(contextPath + "/vehicle/{id}"), handler::get))
                        .and(route(GET(contextPath + "/vehicle/{id}/gps"), handler::getGps))
                        .and(route(PUT(contextPath + "/vehicle/{id}"), handler::update))
                        .and(route(GET(contextPath + "/vehicle/{id}/environmentalData"), handler::getEnvironmentalData))
                        .and(route(GET(contextPath + "/vehicle/{id}/predictedEnvironmentalData"), handler::getPredictedEnvironmentalData));
    }

    private RouterFunction<ServerResponse> vehicleActionRoutes(VehicleActionHandler handler) {
        return
                route(POST(contextPath + "/setTemperatureLimit"), handler::setTemperatureLimit)
                        .and(route(POST(contextPath + "/setHumidityLimit"), handler::setHumidityLimit))
                        .and(route(POST(contextPath + "/setLightIntensityLimit"), handler::setLightIntensityLimit))
                        .and(route(POST(contextPath + "/rotateVehicleLeft"), handler::rotateVehicleLeft))
                        .and(route(POST(contextPath + "/rotateVehicleRight"), handler::rotateVehicleRight))
                        .and(route(POST(contextPath + "/moveVehicleForward"), handler::moveVehicleForward));
    }

    public static void main(String[] args) {
        SpringApplication.run(VehiclemanagementserviceApplication.class, args);
    }

}

