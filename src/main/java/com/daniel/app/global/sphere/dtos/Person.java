package com.daniel.app.global.sphere.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString()
public class Person {
    private String username;
    private Long userId;
    private String jobTitle;
    private String occupation;

}
