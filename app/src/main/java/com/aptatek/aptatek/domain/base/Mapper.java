package com.aptatek.aptatek.domain.base;

import java.util.List;

/**
 * Utility to provide mapping between data and domain layer.
 *
 * @param <I> The type of domain model
 * @param <O> The type of data model
 */
// TODO use this maybe for data/mappers ...
public interface Mapper<I, O> {

    List<I> mapListToDomain(List<O> dataModels);

    I mapToDomain(O dataModel);

    List<O> mapListToData(List<I> domainModels);

    O mapToData(I domainModel);

}
