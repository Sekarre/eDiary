package com.ediary.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('TEACHER', 'DEPUTY_HEAD', 'HEADMASTER')" +
        " AND @userAuthenticationManager.userIdMatches(authentication, #userId)")
public @interface NoticePermission {
}
