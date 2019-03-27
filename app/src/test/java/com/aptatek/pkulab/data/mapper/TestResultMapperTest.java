package com.aptatek.pkulab.data.mapper;

import com.aptatek.pkulab.data.model.TestResultDataModel;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.reader.TestResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class TestResultMapperTest {

    private TestResultMapper mapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mapper = new TestResultMapper();
    }

    @Test
    public void testMapToDomain() throws Exception {
        final TestResultDataModel dataModel = new TestResultDataModel();
        dataModel.setFasting(false);
        dataModel.setSick(true);
        dataModel.setId("dummy");
        dataModel.setPkuLevel(PkuLevel.create(20f, PkuLevelUnits.MICRO_MOL));
        dataModel.setReaderId("dummy");
        dataModel.setTimestamp(System.currentTimeMillis() - 1000L);

        final TestResult domainModel = mapper.mapToDomain(dataModel);
        compare(dataModel, domainModel);
    }

    private void compare(TestResultDataModel dataModel, TestResult domainModel) {
        assert dataModel.getId().equals(domainModel.getId());
        assert dataModel.getPkuLevel().equals(domainModel.getPkuLevel());
        assert dataModel.getReaderId().equals(domainModel.getReaderId());
        assert dataModel.getTimestamp() == domainModel.getTimestamp();
        assert dataModel.isFasting() == domainModel.isFasting();
        assert dataModel.isSick() == domainModel.isSick();
    }

    @Test
    public void testMapToDomainList() throws Exception {
        final List<TestResultDataModel> dataModels = new ArrayList<>();
        final TestResultDataModel dataModel = new TestResultDataModel();
        dataModel.setFasting(false);
        dataModel.setSick(true);
        dataModel.setId("dummy");
        dataModel.setPkuLevel(PkuLevel.create(20f, PkuLevelUnits.MICRO_MOL));
        dataModel.setReaderId("dummy");
        dataModel.setTimestamp(System.currentTimeMillis() - 1000L);
        dataModels.add(dataModel);
        final TestResultDataModel dataModel2 = new TestResultDataModel();
        dataModel2.setFasting(true);
        dataModel2.setSick(false);
        dataModel2.setId("two");
        dataModel2.setPkuLevel(PkuLevel.create(0.5f, PkuLevelUnits.MILLI_GRAM));
        dataModel2.setReaderId("reader2");
        dataModel2.setTimestamp(System.currentTimeMillis() + 1000L);
        dataModels.add(dataModel2);

        final List<TestResult> domainModels = mapper.mapListToDomain(dataModels);
        for (int i = 0; i < domainModels.size(); i++) {
            compare(dataModels.get(i), domainModels.get(i));
        }

    }

    @Test
    public void testToData() throws Exception {
        final TestResult domainModel = TestResult.builder()
                .setFasting(false)
                .setSick(true)
                .setId("dummy")
                .setReaderId("dummy")
                .setPkuLevel(PkuLevel.create(20f, PkuLevelUnits.MICRO_MOL))
                .setTimestamp(System.currentTimeMillis() - 1000L)
                .build();

        final TestResultDataModel dataModel = mapper.mapToData(domainModel);

        compare(dataModel, domainModel);
    }

    @Test
    public void testToDataList() throws Exception {
        final List<TestResult> domainModels = new ArrayList<>();
        final TestResult domainModel = TestResult.builder()
                .setFasting(false)
                .setSick(true)
                .setId("dummy")
                .setReaderId("dummy")
                .setPkuLevel(PkuLevel.create(20f, PkuLevelUnits.MICRO_MOL))
                .setTimestamp(System.currentTimeMillis() - 1000L)
                .build();
        domainModels.add(domainModel);
        final TestResult domainModel2 = TestResult.builder()
                .setFasting(true)
                .setSick(false)
                .setId("two")
                .setReaderId("reader2")
                .setPkuLevel(PkuLevel.create(0.5f, PkuLevelUnits.MILLI_GRAM))
                .setTimestamp(System.currentTimeMillis() + 1000L)
                .build();
        domainModels.add(domainModel2);

        final List<TestResultDataModel> dataModels = mapper.mapListToData(domainModels);
        for (int i = 0; i < dataModels.size(); i++) {
            compare(dataModels.get(i), domainModels.get(i));
        }
    }



}
