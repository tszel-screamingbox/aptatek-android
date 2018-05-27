package com.aptatek.aptatek.domain.base;

import java.util.Map;

import javax.inject.Provider;

/**
 * Base class to provide an abstraction over the data access layer.
 * Interactors should get the data through Repository subclasses.
 */
public abstract class Repository<Domain, Data> {

    protected final Mapper<Domain, Data> mapper;

    protected Repository(Provider<Map<Class<?>, Mapper<?,?>>> mapperProvider) {
        mapper = (Mapper<Domain, Data>) mapperProvider.get().get(getDomainClass());
    }

    protected abstract Class<?> getDomainClass();

}
