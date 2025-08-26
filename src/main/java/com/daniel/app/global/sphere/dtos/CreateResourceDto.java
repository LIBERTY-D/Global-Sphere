package com.daniel.app.global.sphere.dtos;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateResourceDto {

    private String title;
    private String description;
    private String content;
    private MultipartFile image;
    private String externalUrl;

}
