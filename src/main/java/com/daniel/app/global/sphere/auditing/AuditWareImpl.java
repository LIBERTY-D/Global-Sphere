package com.daniel.app.global.sphere.auditing;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component("auditWareImpl")
public class AuditWareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            // No authentication at all (startup, scheduled job, etc.)
            return Optional.of("app");
        }

        if (!authentication.isAuthenticated()) {
            // Not authenticated
            return Optional.of("anonymous");
        }

        if (authentication instanceof AnonymousAuthenticationToken) {
            // Explicit anonymous auth (e.g. Spring Security anonymous user)
            return Optional.of("anonymous");
        }

        return Optional.ofNullable(authentication.getName());
    }
}
