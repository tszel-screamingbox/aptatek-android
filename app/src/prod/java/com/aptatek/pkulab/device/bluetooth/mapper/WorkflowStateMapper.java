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
            case "POST-TEST": {
                return WorkflowState.POST_TEST;
            }
            case "READING CASSETTE": {
                return WorkflowState.READING_CASSETTE;
            }
            case "DETECTING FLUID": {
                return WorkflowState.DETECTING_FLUID;
            }
            case "SELF-TEST": {
                return WorkflowState.SELF_TEST;
            }
            case "USED CASSETTE ERROR": {
                return WorkflowState.USED_CASSETTE_ERROR;
            }
            case "CASSETTE REMOVED ERROR": {
                return WorkflowState.CASSETTE_REMOVED_ERROR;
            }
            case "POWER ERROR": {
                return WorkflowState.POWER_ERROR;
            }
            case "FLUID DETECTION ERROR": {
                return WorkflowState.FLUID_DETECTION_ERROR;
            }
            case "FLUID ALREADY PRESENT ERROR": {
                return WorkflowState.FLUID_ALREADY_PRESENT_ERROR;
            }
            case "INVALID CASSETTE ERROR": {
                return WorkflowState.INVALID_CASSETTE_ERROR;
            }
            case "CONTAMINATED CASSETTE ERROR": {
                return WorkflowState.CONTAMINATED_CASSETTE_ERROR;
            }
            case "EXPIRED CASSETTE ERROR": {
                return WorkflowState.EXPIRED_CASSETTE_ERROR;
            }
            case "ENVIRONMENT ERROR": {
                return WorkflowState.ENVIRONMENT_ERROR;
            }
            case "HARDWARE ERROR": {
                return WorkflowState.HARDWARE_ERROR;
            }
            case "SYSTEM MEMORY ERROR": {
                return WorkflowState.SYSTEM_MEMORY_ERROR;
            }
            case "TIME ERROR": {
                return WorkflowState.TIME_ERROR;
            }
            case "SYSTEM ERROR": {
                return WorkflowState.SYSTEM_ERROR;
            }
            case "TEST ERROR": {
                return WorkflowState.TEST_ERROR;
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
