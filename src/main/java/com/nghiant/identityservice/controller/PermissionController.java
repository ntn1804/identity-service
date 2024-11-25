package com.nghiant.identityservice.controller;

import com.nghiant.identityservice.dto.request.PermissionRequest;
import com.nghiant.identityservice.dto.response.ApiResponse;
import com.nghiant.identityservice.dto.response.PermissionResponse;
import com.nghiant.identityservice.service.PermissionService;
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
@RequestMapping("/permissions")
public class PermissionController {

  private final PermissionService permissionService;

  @PostMapping
  ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
    ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(permissionService.createPermission(request));
    return apiResponse;
  }

  @GetMapping
  ApiResponse<List<PermissionResponse>> getPermissions() {
    ApiResponse<List<PermissionResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setResult(permissionService.getPermissions());
    return apiResponse;

  }

  @DeleteMapping("/{permissionName}")
  ApiResponse<Void> deletePermission(@PathVariable String permissionName) {
    permissionService.deletePermission(permissionName);
    return new ApiResponse<>();
  }
}
