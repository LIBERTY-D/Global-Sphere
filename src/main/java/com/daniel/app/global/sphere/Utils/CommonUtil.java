package com.daniel.app.global.sphere.Utils;

import com.daniel.app.global.sphere.dtos.CreateResourceDto;
import com.daniel.app.global.sphere.dtos.EditResourceDto;
import com.daniel.app.global.sphere.exceptions.InvalidLinkException;

import java.security.SecureRandom;
import java.util.Base64;

public class CommonUtil {
    public static String checkLinkValidation(String link, CreateResourceDto form) {
        if (link == null || link.trim().isEmpty()) {
            return null;
        }
        if (!link.startsWith("http://") && !link.startsWith("https://")) {
            throw new InvalidLinkException("Invalid: Link must start with http:// or https://", form);
        }

        return link;
    }

    public static String checkLinkValidation(String link, EditResourceDto form) {
        if (link == null || link.trim().isEmpty()) {
            return null;
        }
        if (!link.startsWith("http://") && !link.startsWith("https://")) {
            throw new InvalidLinkException("Invalid: Link must start with " +
                    "http:// or https://", form);
        }

        return link;
    }

    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String getEmailLinkResetPassword(String token) {
        return token;
    }

}
