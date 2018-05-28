package com.aptatek.aptatek.domain.base;

import java.util.Collection;

/**
 * Utility to provide mapping between data and domain layer.
 *
 * @param <DomainModel> The type of domain model
 * @param <DataModel> The type of data model
 */
public interface Mapper<DomainModel, DataModel> {

    Collection<DomainModel> mapListToDomain(Collection<DataModel> dataModels);
    DomainModel mapToDomain(DataModel dataModel);

    Collection<DataModel> mapListToData(Collection<DomainModel> domainModels);
    DataModel mapToData(DomainModel domainModel);

}
