package com.daniel.app.global.sphere.Utils;

import com.daniel.app.global.sphere.dtos.CreateResourceDto;
import com.daniel.app.global.sphere.dtos.EditResourceDto;
import com.daniel.app.global.sphere.exceptions.InvalidLinkException;

public class CommonUtil {
    public static String checkLinkValidation(String link, CreateResourceDto form) {
        if (link == null || link.trim().isEmpty()) {
            return null;
        }
        if (!link.startsWith("http://") && !link.startsWith("https://")) {
            throw new InvalidLinkException("Invalid: Link must start with " + "http:// or https://", form);
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

}
