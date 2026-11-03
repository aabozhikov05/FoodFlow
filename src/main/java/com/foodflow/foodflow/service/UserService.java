package com.foodflow.foodflow.service;

import com.foodflow.foodflow.dto.*;
import com.foodflow.foodflow.entity.User;
import com.foodflow.foodflow.repository.UserRepository;
import com.foodflow.foodflow.security.JwtService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }
    public User createUser(User user){
        return userRepository.save(user);
    }
    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered"
            );
        }
        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail()
        );

        return response;
    }
    public UserResponse getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
    public Page<UserResponse> getAllUsers(int page, int size) {

        Page<User> users = userRepository.findAll(
                PageRequest.of(page, size)
        );

        return users.map(user -> new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        ));
    }
    public UserResponse updateUser(int id, UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        User updatedUser = userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getEmail()
        );
    }
    public void deleteUser(int id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        userRepository.delete(user);
    }
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}

