package com.nghiant.identityservice.service.impl;

import com.nghiant.identityservice.dto.request.UserCreationRequest;
import com.nghiant.identityservice.dto.request.UserUpdateRequest;
import com.nghiant.identityservice.dto.response.UserResponse;
import com.nghiant.identityservice.entity.User;
import com.nghiant.identityservice.exception.AppException;
import com.nghiant.identityservice.exception.ErrorCode;
import com.nghiant.identityservice.mapper.UserMapper;
import com.nghiant.identityservice.repository.UserRepository;
import com.nghiant.identityservice.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserResponse createUser(UserCreationRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }

    User user = userMapper.toUser(request);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    userRepository.save(user);

    return userMapper.toUserResponse(user);
  }

  @Override
  public UserResponse getUser(String userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    return userMapper.toUserResponse(user);
  }

  @Override
  public List<UserResponse> getUsers() {
    List<User> userList = userRepository.findAll();
    List<UserResponse> responseList = new ArrayList<>();

    for (User user : userList) {
      responseList.add(userMapper.toUserResponse(user));
    }

    return responseList;
  }

  @Override
  public UserResponse updateUser(String userId, UserUpdateRequest request) {
    User existedUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    userMapper.updateUser(existedUser, request);

    userRepository.save(existedUser);

    return userMapper.toUserResponse(existedUser);
  }

  @Override
  public String deleteUser(String userId) {
    User existedUser = userRepository.findById(userId)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    userRepository.delete(existedUser);
    return "User deleted successfully";
  }
}
