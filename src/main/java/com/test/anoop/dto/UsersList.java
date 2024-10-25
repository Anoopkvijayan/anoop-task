package com.test.anoop.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@Getter
@Setter
public class UsersList {
    private Integer id;
    private String name;
    private String email;
    private String roles;
    private String gender;

    public UsersList() {
        this.id = 0;
        this.name = "";
        this.email = "";
        this.roles = "";
        this.gender = "";
    }

}
