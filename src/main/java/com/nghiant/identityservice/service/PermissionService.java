package com.nghiant.identityservice.service;

import com.nghiant.identityservice.dto.request.PermissionRequest;
import com.nghiant.identityservice.dto.response.PermissionResponse;
import java.util.List;

public interface PermissionService {

  PermissionResponse createPermission(PermissionRequest request);
  List<PermissionResponse> getPermissions();
  void deletePermission(String permissionName);
}
