package com.nghiant.identityservice.mapper;

import com.nghiant.identityservice.dto.request.PermissionRequest;
import com.nghiant.identityservice.dto.response.PermissionResponse;
import com.nghiant.identityservice.entity.Permission;
import org.mapstruct.Mapper;

@Mapper
public interface PermissionMapper {

  Permission toPermission(PermissionRequest request);

  PermissionResponse toPermissionResponse(Permission permission);
}
