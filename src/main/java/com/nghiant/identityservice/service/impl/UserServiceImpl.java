package com.nghiant.identityservice.service.impl;

import com.nghiant.identityservice.dto.request.UserCreationRequest;
import com.nghiant.identityservice.dto.request.UserUpdateRequest;
import com.nghiant.identityservice.dto.response.UserResponse;
import com.nghiant.identityservice.entity.Role;
import com.nghiant.identityservice.entity.User;
import com.nghiant.identityservice.exception.AppException;
import com.nghiant.identityservice.exception.ErrorCode;
import com.nghiant.identityservice.mapper.UserMapper;
import com.nghiant.identityservice.repository.RoleRepository;
import com.nghiant.identityservice.repository.UserRepository;
import com.nghiant.identityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var role = roleRepository.findById("USER")
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_ERROR));

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);
        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {

        // Get current user by token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findUserByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User existedUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(existedUser, request);
        existedUser.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        existedUser.setRoles(new HashSet<>(roles));

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
