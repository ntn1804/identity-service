package com.nghiant.identityservice.service.impl;

import com.nghiant.identityservice.dto.request.UserCreationRequest;
import com.nghiant.identityservice.dto.request.UserUpdateRequest;
import com.nghiant.identityservice.entity.User;
import com.nghiant.identityservice.exception.AppException;
import com.nghiant.identityservice.exception.ErrorCode;
import com.nghiant.identityservice.repository.UserRepository;
import com.nghiant.identityservice.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public User createUser(UserCreationRequest request) {
      if (userRepository.existsByUsername(request.getUsername())) {
          throw new AppException(ErrorCode.USER_EXISTED);
      }

    User user = new User();

    user.setUsername(request.getUsername());
    user.setPassword(request.getPassword());
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setDob(request.getDob());

    return userRepository.save(user);
  }

  @Override
  public User getUser(String userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }

  @Override
  public List<User> getUsers() {
    return userRepository.findAll();
  }

  @Override
  public User updateUser(String userId, UserUpdateRequest request) {
    User existedUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    existedUser.setPassword(request.getPassword());
    existedUser.setFirstName(request.getFirstName());
    existedUser.setLastName(request.getLastName());
    existedUser.setDob(request.getDob());

    return userRepository.save(existedUser);
  }

  @Override
  public String deleteUser(String userId) {
    User existedUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    userRepository.delete(existedUser);
    return "User deleted successfully";
  }
}
