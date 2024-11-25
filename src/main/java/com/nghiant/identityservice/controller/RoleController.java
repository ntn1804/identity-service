package com.nghiant.identityservice.controller;

import com.nghiant.identityservice.dto.request.PermissionRequest;
import com.nghiant.identityservice.dto.request.RoleRequest;
import com.nghiant.identityservice.dto.response.ApiResponse;
import com.nghiant.identityservice.dto.response.PermissionResponse;
import com.nghiant.identityservice.dto.response.RoleResponse;
import com.nghiant.identityservice.service.PermissionService;
import com.nghiant.identityservice.service.RoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

  private final RoleService roleService;

  @PostMapping
  ApiResponse<RoleResponse> createPermission(@RequestBody RoleRequest request) {
    ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(roleService.createRole(request));
    return apiResponse;
  }

  @GetMapping
  ApiResponse<List<RoleResponse>> getPermissions() {
    ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setResult(roleService.getRoles());
    return apiResponse;

  }

  @DeleteMapping("/{roleName}")
  ApiResponse<Void> deletePermission(@PathVariable String roleName) {
    roleService.deleteRole(roleName);
    return new ApiResponse<>();
  }
}
