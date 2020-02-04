package at.htl.vehiclemanagementservice.handler;

import at.htl.vehiclemanagementservice.model.User;
import at.htl.vehiclemanagementservice.service.web.UserManagerService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
public class UserHandler {

    private final UserManagerService userManagerService;

    @Autowired
    public UserHandler(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    @NotNull
    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(User.class)
                .map(userManagerService::login)
                .flatMap(mb -> mb.map(b -> b ? ok().build() : status(401).build()))
                .flatMap(s -> s);
    }
}
