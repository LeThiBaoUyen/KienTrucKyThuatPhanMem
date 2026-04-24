package com.minifood.foodservice.common.security;

import com.minifood.foodservice.common.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

@Component
public class RoleGuard {

    public void requireAdmin(String roleHeader) {
        if (roleHeader == null || !"ADMIN".equalsIgnoreCase(roleHeader.trim())) {
            throw new UnauthorizedException("Ban khong co quyen truy cap tai nguyen nay");
        }
    }
}
