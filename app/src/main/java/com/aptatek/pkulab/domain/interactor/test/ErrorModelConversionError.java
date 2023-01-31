package com.aptatek.pkulab.domain.interactor.test;

import com.aptatek.pkulab.domain.model.reader.WorkflowState;

public class ErrorModelConversionError extends Throwable {

    private final WorkflowState workflowState;
    private final String errorCharReading;

    public ErrorModelConversionError(WorkflowState workflowState, String errorCharReading) {
        this.workflowState = workflowState;
        this.errorCharReading = errorCharReading;
    }

    public WorkflowState getWorkflowState() {
        return workflowState;
    }

    public String getErrorCharReading() {
        return errorCharReading;
    }

    @Override
    public String toString() {
        return "ErrorModelConversionError{" +
                "workflowState=" + workflowState +
                ", errorCharReading='" + errorCharReading + '\'' +
                '}';
    }
}
