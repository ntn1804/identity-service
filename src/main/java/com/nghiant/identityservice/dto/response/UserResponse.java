package com.nghiant.identityservice.dto.response;

import java.time.LocalDate;
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

}
