package com.iba.iot.datasimulator.common.spring;

import org.springframework.messaging.simp.user.DefaultUserDestinationResolver;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.util.PathMatcher;

/**
 *
 */
public class CustomUserDestinationResolver extends DefaultUserDestinationResolver {

    public CustomUserDestinationResolver(SimpUserRegistry userRegistry) {
        super(userRegistry);
    }

    @Override
    public void setPathMatcher(PathMatcher pathMatcher) {}
}
