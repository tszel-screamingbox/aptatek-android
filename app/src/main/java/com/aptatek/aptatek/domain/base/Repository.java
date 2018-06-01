package com.aptatek.aptatek.domain.base;

import java.util.Map;

import javax.inject.Provider;

/**
 * Base class to provide an abstraction over the data access layer.
 * Interactors should get the data through Repository subclasses.
 */
public abstract class Repository<I, O> {

    protected final Mapper<I, O> mapper;

    @SuppressWarnings("unchecked")
    protected Repository(final Provider<Map<Class<?>, Mapper<?,?>>> mapperProvider) {
        mapper = (Mapper<I, O>) mapperProvider.get().get(getDomainClass());
    }

    protected abstract Class<?> getDomainClass();

}
