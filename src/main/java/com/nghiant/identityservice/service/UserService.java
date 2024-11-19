package com.nghiant.identityservice.service;

import com.nghiant.identityservice.dto.request.UserCreationRequest;
import com.nghiant.identityservice.dto.request.UserUpdateRequest;
import com.nghiant.identityservice.dto.response.UserResponse;
import com.nghiant.identityservice.entity.User;
import java.util.List;

public interface UserService {

  UserResponse createUser(UserCreationRequest request);

  UserResponse getUser(String userId);

  List<UserResponse> getUsers();

  UserResponse updateUser(String userId, UserUpdateRequest request);

  String deleteUser(String userId);
}
