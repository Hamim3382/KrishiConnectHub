package com.krishiconnecthub.service.impl;

import com.krishiconnecthub.dto.UserDTO;
import com.krishiconnecthub.model.User;
import com.krishiconnecthub.repository.UserRepository;
import com.krishiconnecthub.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(UserDTO userDTO) {
        userRepository.findByEmail(userDTO.getEmail()).ifPresent(u -> {
            throw new IllegalStateException("Email already in use.");
        });

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        // In a real application, always hash the password
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());

        return userRepository.save(user);
    }

    @Override
    public Optional<User> login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        // In a real application, use a password encoder to compare hashed passwords
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}