package com.aptatek.pkulab.domain.interactor.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.error.ErrorModel;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class TestErrorInteractor {

    private final ResourceInteractor resourceInteractor;

    @Inject
    public TestErrorInteractor(final ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
    }

    @NotNull
    public ErrorModel createErrorModel(@NonNull WorkflowState workflowState, @Nullable String errorCharReading) throws ErrorModelConversionError {
        final ErrorModel.Builder errorModelBuilder = ErrorModel.builder();

        switch (workflowState) {
            case ENVIRONMENT_ERROR:
            case HARDWARE_ERROR:
            case SYSTEM_ERROR:
            case TEST_ERROR: {
                errorModelBuilder.setTitle(resourceInteractor.getStringResource(R.string.error_title_generic_2))
                        .setMessage(resourceInteractor.getStringResource(R.string.error_message_generic_2));
                break;
            }
            case INVALID_CASSETTE_ERROR:
            case CONTAMINATED_CASSETTE_ERROR:
            case EXPIRED_CASSETTE_ERROR: {
                errorModelBuilder.setTitle(resourceInteractor.getStringResource(R.string.error_title_generic_1))
                        .setMessage(resourceInteractor.getStringResource(R.string.error_message_generic_1));
                break;
            }
            default: {
                throw new ErrorModelConversionError(workflowState, errorCharReading);
            }
        }


        return errorModelBuilder.build();
    }

}