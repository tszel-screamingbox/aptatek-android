package com.aptatek.pkulab.device.bluetooth.mapper;

import com.aptatek.pkulab.device.bluetooth.model.WorkflowStateResponse;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;

import java.util.List;

import javax.inject.Inject;

import ix.Ix;

public class WorkflowStateMapper implements Mapper<WorkflowState, WorkflowStateResponse> {

    @Inject
    public WorkflowStateMapper() {
    }

    @Override
    public List<WorkflowState> mapListToDomain(final List<WorkflowStateResponse> dataModels) {
        return Ix.from(dataModels).map(this::mapToDomain).toList();
    }

    @Override
    public WorkflowState mapToDomain(final WorkflowStateResponse dataModel) {
        switch (dataModel.getState()) {
            case "TEST RUNNING": {
                return WorkflowState.TEST_RUNNING;
            }
            case "TEST COMPLETE": {
                return WorkflowState.TEST_COMPLETE;
            }
            case "READY": {
                return WorkflowState.READY;
            }
            case "default":
            default: {
                return WorkflowState.DEFAULT;
            }
        }
    }

    @Override
    public List<WorkflowStateResponse> mapListToData(final List<WorkflowState> domainModels) {
        return Ix.from(domainModels).map(this::mapToData).toList();
    }

    @Override
    public WorkflowStateResponse mapToData(final WorkflowState domainModel) {
        return new WorkflowStateResponse(); // not used anyway
    }
}
