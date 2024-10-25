package com.test.anoop.dto;


import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    boolean success;
    String token;

    public LoginResponse() {
        this.token = "";
    }
}
