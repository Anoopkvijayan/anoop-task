package com.test.anoop.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@Getter
@Setter
public class RegResponse {
    Integer id;
    String message;

    public RegResponse() {
        this.id = 0;
        this.message = "";
    }
}
