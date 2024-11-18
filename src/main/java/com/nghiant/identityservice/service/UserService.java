package com.nghiant.identityservice.service;

import com.nghiant.identityservice.dto.request.UserCreationRequest;
import com.nghiant.identityservice.dto.request.UserUpdateRequest;
import com.nghiant.identityservice.entity.User;
import java.util.List;

public interface UserService {
  User createUser(UserCreationRequest request);
  User getUser(String userId);
  List<User> getUsers();
  User updateUser(String userId, UserUpdateRequest request);
  String deleteUser(String userId);
}
