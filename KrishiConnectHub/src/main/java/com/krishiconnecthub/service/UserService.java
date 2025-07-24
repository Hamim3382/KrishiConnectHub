package com.krishiconnecthub.service;

import com.krishiconnecthub.dto.UserDTO;
import com.krishiconnecthub.model.User;

import java.util.Optional;

public interface UserService {
    /**
     * Registers a new user.
     * @param userDTO The user data transfer object.
     * @return The created User.
     * @throws IllegalStateException if the email is already taken.
     */
    User register(UserDTO userDTO);

    /**
     * Authenticates a user.
     * @param email The user's email.
     * @param password The user's password.
     * @return An Optional containing the User if authentication is successful, otherwise empty.
     */
    Optional<User> login(String email, String password);

    /**
     * Finds a user by their ID.
     * @param id The ID of the user.
     * @return An Optional containing the User if found, otherwise empty.
     */
    Optional<User> findById(Long id);
}