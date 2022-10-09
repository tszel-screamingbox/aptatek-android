package com.aptatek.pkulab.domain.interactor.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.error.ErrorModel;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import javax.inject.Inject;

public class ErrorInteractor {

    private final ResourceInteractor resourceInteractor;

    @Inject
    public ErrorInteractor(final ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
    }

    @NotNull
    public ErrorModel createErrorModel(@NonNull WorkflowState workflowState, @Nullable String errorCharReading) throws ErrorModelConversionError {
        final ErrorModel.Builder errorModelBuilder = ErrorModel.builder();

        switch (workflowState) {
            case ENVIRONMENT_ERROR:
            case HARDWARE_ERROR:
            case SYSTEM_ERROR:
            case POWER_ERROR:
            case TEST_ERROR: {
                errorModelBuilder
                        .setAfterChamberScrewedOn(false)
                        .setTitle(resourceInteractor.getStringResource(R.string.error_title_generic_1))
                        .setMessage(resourceInteractor.getStringResource(R.string.error_message_generic_1));
                break;
            }
            case USED_CASSETTE_ERROR: {
                errorModelBuilder
                        .setAfterChamberScrewedOn(true)
                        .setTitle(resourceInteractor.getStringResource(R.string.test_alert_used_cassette_title))
                        .setMessage(resourceInteractor.getStringResource(R.string.test_alert_used_cassette_message));
                break;
            }
            case INVALID_CASSETTE_ERROR:
            case CONTAMINATED_CASSETTE_ERROR:
            case EXPIRED_CASSETTE_ERROR: {
                errorModelBuilder
                        .setAfterChamberScrewedOn(true)
                        .setTitle(resourceInteractor.getStringResource(R.string.error_title_generic_2))
                        .setMessage(resourceInteractor.getStringResource(R.string.error_message_generic_2));
                break;
            }
            default: {
//                throw new ErrorModelConversionError(workflowState, errorCharReading);
                if (workflowState.name().toLowerCase(Locale.getDefault()).contains("error")) {
                    // default to generic 1
                    errorModelBuilder
                            .setAfterChamberScrewedOn(false)
                            .setTitle(resourceInteractor.getStringResource(R.string.error_title_generic_1))
                            .setMessage(resourceInteractor.getStringResource(R.string.error_message_generic_1));
                    break;
                } else {
                    throw new ErrorModelConversionError(workflowState, errorCharReading);
                }
            }
        }

        errorModelBuilder.setErrorCode(workflowState.name());
        return errorModelBuilder.build();
    }

}
