package com.aptatek.pkulab.injection.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import dagger.MapKey;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
@MapKey
public @interface ClassKey {
    Class<?> value();
}
