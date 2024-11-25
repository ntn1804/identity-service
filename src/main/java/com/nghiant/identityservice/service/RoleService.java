package com.nghiant.identityservice.service;

import com.nghiant.identityservice.dto.request.PermissionRequest;
import com.nghiant.identityservice.dto.request.RoleRequest;
import com.nghiant.identityservice.dto.response.PermissionResponse;
import com.nghiant.identityservice.dto.response.RoleResponse;
import java.util.List;

public interface RoleService {

  RoleResponse createRole(RoleRequest request);
  List<RoleResponse> getRoles();
  void deleteRole(String roleName);
}
