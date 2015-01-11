// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.boundary;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Daria Stepanova
 */
public class Dependencies {

    public static <T> T checkDependency(@Nonnull final T dependency) {
        checkArgument(dependency != null);
        return dependency;
    }
}
