package com.aptatek.pkulab.domain.interactor.parentalgate;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.model.AgeCheckModel;
import com.aptatek.pkulab.domain.model.AgeCheckResult;

import javax.inject.Inject;

import io.reactivex.Single;

public class ParentalGateInteractor {

    private static final int MIN_AGE = 13;
    private final BirthDateFormatter birthDateFormatter;

    @Inject
    ParentalGateInteractor(final BirthDateFormatter birthDateFormatter) {
        this.birthDateFormatter = birthDateFormatter;
    }

    @NonNull
    public Single<String> formatBirthDate(final long birthDate) {
        return Single.fromCallable(() -> birthDateFormatter.formatBirthDate(birthDate));
    }

    @NonNull
    public Single<AgeCheckResult> verify(@NonNull final AgeCheckModel ageCheckModel) {
        return Single.fromCallable(() -> {
            final int calculatedAge = TimeHelper.diffInYears(ageCheckModel.getBirthDate(), System.currentTimeMillis());

            if (calculatedAge < MIN_AGE) {
                return AgeCheckResult.NOT_OLD_ENOUGH;
            } else if (calculatedAge != ageCheckModel.getAge()) {
                return AgeCheckResult.AGE_NOT_MATCH;
            } else {
                return AgeCheckResult.VALID_AGE;
            }
        });
    }
}
