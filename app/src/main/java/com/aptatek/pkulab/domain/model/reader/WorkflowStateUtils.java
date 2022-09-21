package com.aptatek.pkulab.domain.model.reader;

public class WorkflowStateUtils {

    public static boolean isErrorState(final WorkflowState workflowState) {
        return workflowState.name().toLowerCase().contains("error");
    }

}
