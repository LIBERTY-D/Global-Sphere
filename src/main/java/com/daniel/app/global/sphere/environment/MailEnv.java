package com.daniel.app.global.sphere.environment;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Getter
@Setter
public class MailEnv {
    private Integer port;
    private String smtp;
    private String username;
    private String password;
    private String mailResetPasswordUrl;

}