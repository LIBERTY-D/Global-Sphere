package com.daniel.app.global.sphere.dtos;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EditResourceDto {
    private String title;
    private String description;
    private String content;
    private MultipartFile image;
    private String externalUrl;
}
