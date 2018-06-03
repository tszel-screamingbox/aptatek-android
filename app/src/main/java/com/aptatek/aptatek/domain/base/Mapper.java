package com.aptatek.aptatek.domain.base;

import java.util.Collection;

/**
 * Utility to provide mapping between data and domain layer.
 *
 * @param <I> The type of domain model
 * @param <O> The type of data model
 */
public interface Mapper<I, O> {

    Collection<I> mapListToDomain(Collection<O> dataModels);

    I mapToDomain(O dataModel);

    Collection<O> mapListToData(Collection<I> domainModels);

    O mapToData(I domainModel);

}
