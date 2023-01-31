package com.aptatek.pkulab.injection.qualifier;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import dagger.MapKey;

@Target(METHOD)
@Retention(RUNTIME)
@MapKey
public @interface ClassKey {
    Class<?> value();
}
