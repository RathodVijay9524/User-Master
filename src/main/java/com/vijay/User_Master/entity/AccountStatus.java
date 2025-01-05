package com.vijay.User_Master.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class AccountStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Indicates whether the account is active or not
    private Boolean isActive;

    // Code used for verifying the account during registration
    private String verificationCode;

    // Token used for password reset via email link
    private String passwordResetToken;

    /*
     * OTP (One-Time Password) used for resetting the password via email link.
     * When a user requests to reset their password, an OTP is sent to their email address.
     * The email contains a link that directs the user to a frontend page where they can enter the OTP.
     * If the OTP verification is successful, the user is then navigated to a page where they can reset their password.
     * The reset password page allows the user to enter a new password and confirm it before submitting.
     */
    private String otp;
}


/*
 *   for reset password by otp with mail link, send otp,
 *   by clicking on link get front-end side page you can enter opt and submit
 *   if verification is success
 *   then you can navigate to next page like reset password -
 *   then you can enter newPassword and confirm password
 *
 * */

