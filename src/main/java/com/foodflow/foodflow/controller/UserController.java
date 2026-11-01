package com.foodflow.foodflow.controller;

import com.foodflow.foodflow.dto.UserRegistrationRequest;
import com.foodflow.foodflow.dto.UserResponse;
import com.foodflow.foodflow.dto.UserUpdateRequest;
import com.foodflow.foodflow.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        return userService.registerUser(request);
    }
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }
    @GetMapping
    public Page<UserResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return userService.getAllUsers(page, size);
    }
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable int id,
            @Valid @RequestBody UserUpdateRequest request) {

        return userService.updateUser(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}