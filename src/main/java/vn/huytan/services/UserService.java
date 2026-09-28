package vn.huytan.services;

import org.springframework.stereotype.Service;
import vn.huytan.entity.User;
import vn.huytan.repository.UserRepository;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }
}