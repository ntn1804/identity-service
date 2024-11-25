package com.nghiant.identityservice.dto.response;

import com.nghiant.identityservice.entity.Permission;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {

  private String name;
  private String description;
  private Set<Permission> permissions;
}
