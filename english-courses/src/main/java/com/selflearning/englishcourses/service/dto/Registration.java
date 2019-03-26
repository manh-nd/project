package com.selflearning.englishcourses.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class Registration {

    @NotEmpty(message = "registration.error.username.empty")
    @Length(min = 5, max = 45, message = "registration.error.username.length")
    private String username;


    @NotEmpty(message = "registration.error.email.empty")
    @Length(min = 6, max = 80, message = "registration.error.email.length")
    @Email(message = "registration.error.email.email")
    private String email;

    @NotEmpty(message = "registration.error.password.empty")
    @Length(min = 6, max = 32, message = "registration.error.password.length")
    private String password;

    @NotEmpty(message = "registration.error.passwordVerify.empty")
    @Length(min = 6, max = 32, message = "registration.error.passwordVerify.length")
    private String passwordVerify;

    @NotEmpty(message = "registration.error.firstName.empty")
    @Length(max = 45, message = "registration.error.firstName.length")
    private String firstName;

    @NotEmpty(message = "registration.error.lastName.empty")
    @Length(max = 45, message = "registration.error.lastName.length")
    private String lastName;

    @NotNull(message = "registration.error.gender.null")
    private Boolean gender;

}
