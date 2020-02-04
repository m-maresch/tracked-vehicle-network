package at.htl.vehiclemanagementservice.domain;

import at.htl.vehiclemanagementservice.model.User;
import io.vavr.collection.List;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class UserRepository {

    private List<User> users = List.of(new User("user", "user"));

    public Mono<Boolean> isValid(User user) {
        var filteredUsers = users.filter(u -> u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword()));

        return filteredUsers.size() == 1 ? Mono.just(true) : Mono.just(false);
    }
}
