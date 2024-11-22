package com.nghiant.identityservice.controller;

import com.nghiant.identityservice.dto.request.UserCreationRequest;
import com.nghiant.identityservice.dto.request.UserUpdateRequest;
import com.nghiant.identityservice.dto.response.ApiResponse;
import com.nghiant.identityservice.dto.response.UserResponse;
import com.nghiant.identityservice.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
    ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.createUser(request));
    return apiResponse;
  }

  @GetMapping("/{userId}")
  ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
    ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.getUser(userId));
    return apiResponse;
  }

  @GetMapping()
  ApiResponse<List<UserResponse>> getUsers() {
    ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.getUsers());
    return apiResponse;
  }

  @PutMapping("/{userId}")
  ApiResponse<UserResponse> updateUser(@PathVariable("userId") String userId,
      @RequestBody UserUpdateRequest request) {
    ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.updateUser(userId, request));
    return apiResponse;
  }

  @DeleteMapping("/{userId}")
  ApiResponse<String> deleteUser(@PathVariable("userId") String userId) {
    ApiResponse<String> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.deleteUser(userId));
    return apiResponse;
  }
}
