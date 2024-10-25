package com.test.anoop.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@Getter
@Setter
public class DelUserRes {

    String message;

    public DelUserRes() {
        this.message = "";
    }

}
