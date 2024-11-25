package com.nghiant.identityservice.mapper;

import com.nghiant.identityservice.dto.request.PermissionRequest;
import com.nghiant.identityservice.dto.request.RoleRequest;
import com.nghiant.identityservice.dto.response.PermissionResponse;
import com.nghiant.identityservice.dto.response.RoleResponse;
import com.nghiant.identityservice.entity.Permission;
import com.nghiant.identityservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RoleMapper {

  @Mapping(target = "permissions", ignore = true)
  Role toRole(RoleRequest request);

  RoleResponse toRoleResponse(Role role);
}
