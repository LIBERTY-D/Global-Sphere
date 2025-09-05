package com.daniel.app.global.sphere.Utils;

import com.daniel.app.global.sphere.models.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

public class RoleUtil {

    public  static   List<SimpleGrantedAuthority> getRoles(Role ... roles){
        return Arrays.stream(roles).map((role)->new SimpleGrantedAuthority(
                "ROLE_"+role.name())).toList();
    }
}
