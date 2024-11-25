package com.nghiant.identityservice.service.impl;

import com.nghiant.identityservice.dto.request.RoleRequest;
import com.nghiant.identityservice.dto.response.RoleResponse;
import com.nghiant.identityservice.entity.Role;
import com.nghiant.identityservice.mapper.RoleMapper;
import com.nghiant.identityservice.repository.PermissionRepository;
import com.nghiant.identityservice.repository.RoleRepository;
import com.nghiant.identityservice.service.RoleService;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;
  private final PermissionRepository permissionRepository;

  @Override
  public RoleResponse createRole(RoleRequest request) {

    Role role = roleMapper.toRole(request);

    var permissions = permissionRepository.findAllById(request.getPermissions());
    role.setPermissions(new HashSet<>(permissions));

    roleRepository.save(role);

    return roleMapper.toRoleResponse(role);
  }

  @Override
  public List<RoleResponse> getRoles() {

    List<Role> roles = roleRepository.findAll();

    return roles.stream().map(roleMapper::toRoleResponse).toList();
  }

  @Override
  public void deleteRole(String roleName) {
    roleRepository.deleteById(roleName);
  }
}
