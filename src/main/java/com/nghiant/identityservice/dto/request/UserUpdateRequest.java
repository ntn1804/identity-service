package com.nghiant.identityservice.dto.request;

import com.nghiant.identityservice.validator.DobConstraint;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
  private String password;
  private String firstName;
  private String lastName;

  @DobConstraint(min = 18, message = "INVALID_DOB")
  private LocalDate dob;
  private Set<String> roles;

}
