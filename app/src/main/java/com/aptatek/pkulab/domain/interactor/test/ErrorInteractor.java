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
    public ErrorModel createErrorModel(@NonNull WorkflowState workflowState, @Nullable String errorCharReading, boolean isAfterChamberScrewedOn) throws ErrorModelConversionError {
        ErrorModel.Builder builder = ErrorModel.builder();
        if (isAfterChamberScrewedOn) {
            builder.setAfterChamberScrewedOn(true);
            builder.setTitle(resourceInteractor.getStringResource(R.string.error_title_generic_2));
            builder.setMessage(resourceInteractor.getStringResource(R.string.error_message_generic_2));
            builder.setErrorCode(workflowState.name());
        } else {
            switch (workflowState) {
                case EXPIRED_CASSETTE_ERROR:
                case CASSETTE_REMOVED_DURING_HEATING_ERROR:
                case CASSETTE_REMOVED_ERROR:
                case FLUID_ALREADY_PRESENT_ERROR:
                case FLUID_DETECTION_ERROR:
                case INVALID_CASSETTE_ERROR:
                case HEATER_OVER_TEMP_ERROR:
                case TIME_ERROR:
                case USED_CASSETTE_ERROR:
                case CONTAMINATED_CASSETTE_ERROR:
                case TEST_ERROR: {
                    builder.setAfterChamberScrewedOn(true);
                    builder.setTitle(resourceInteractor.getStringResource(R.string.error_title_generic_2));
                    builder.setMessage(resourceInteractor.getStringResource(R.string.error_message_generic_2));
                    builder.setErrorCode(workflowState.name());
                    break;
                }
                default:
                    if (workflowState.name().toLowerCase(Locale.getDefault()).contains("error")) {
                        builder.setAfterChamberScrewedOn(false);
                        builder.setTitle(resourceInteractor.getStringResource(R.string.error_title_generic_2));
                        builder.setMessage(resourceInteractor.getStringResource(R.string.error_message_generic_2));
                        builder.setErrorCode(workflowState.name());
                        break;
                    } else {
                        throw new ErrorModelConversionError(workflowState, errorCharReading);
                    }
            }
        }

        return builder.build();
    }
}