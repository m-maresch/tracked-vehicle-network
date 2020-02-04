package at.htl.vehiclemanagementservice.service.web;

import at.htl.vehiclemanagementservice.domain.UserRepository;
import at.htl.vehiclemanagementservice.dto.vehicle.SimplifiedVehicle;
import at.htl.vehiclemanagementservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.management.InstanceNotFoundException;

@Service
public class UserManagerService {

    private final UserRepository userRepository;

    @Autowired
    public UserManagerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<Boolean> login(User user) {
        return userRepository.isValid(user);
    }
}
