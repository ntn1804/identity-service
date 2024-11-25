package com.nghiant.identityservice.dto.response;

import com.nghiant.identityservice.entity.Role;
import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

  private String userId;
  private String username;
  private String firstName;
  private String lastName;
  private LocalDate dob;
  private Set<Role> roles;

}
