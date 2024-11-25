package com.nghiant.identityservice.service.impl;

import com.nghiant.identityservice.dto.request.PermissionRequest;
import com.nghiant.identityservice.dto.response.PermissionResponse;
import com.nghiant.identityservice.entity.Permission;
import com.nghiant.identityservice.mapper.PermissionMapper;
import com.nghiant.identityservice.repository.PermissionRepository;
import com.nghiant.identityservice.service.PermissionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

  private final PermissionRepository permissionRepository;
  private final PermissionMapper permissionMapper;

  @Override
  public PermissionResponse createPermission(PermissionRequest request) {

    Permission permission = permissionMapper.toPermission(request);
    permissionRepository.save(permission);

    return permissionMapper.toPermissionResponse(permission);
  }

  @Override
  public List<PermissionResponse> getPermissions() {

    List<Permission> permissionList = permissionRepository.findAll();

    return permissionList.stream().map(permissionMapper::toPermissionResponse).toList();
  }

  @Override
  public void deletePermission(String permissionName) {

    permissionRepository.deleteById(permissionName);
  }
}
