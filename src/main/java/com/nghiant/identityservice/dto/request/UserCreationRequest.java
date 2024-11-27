package com.nghiant.identityservice.dto.request;

import com.nghiant.identityservice.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {

  @Size(min = 5, message = "INVALID_USERNAME")
  private String username;
  @Size(min = 3, message = "INVALID_PASSWORD")
  private String password;
  private String firstName;
  private String lastName;

  @DobConstraint(min = 18, message = "INVALID_DOB")
  private LocalDate dob;
}
