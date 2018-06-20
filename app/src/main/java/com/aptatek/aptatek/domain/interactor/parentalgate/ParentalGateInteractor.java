package com.aptatek.aptatek.domain.interactor.parentalgate;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.model.AgeCheckModel;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.Single;

public class ParentalGateInteractor {

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
    public Single<Boolean> verify(@NonNull final AgeCheckModel ageCheckModel) {
        return Single.fromCallable(() -> calculateAge(ageCheckModel.getBirthDate()) == ageCheckModel.getAge());
    }

    private int calculateAge(final long timestamp) {
        final Calendar now = Calendar.getInstance();
        final Calendar birthDate = Calendar.getInstance();
        birthDate.setTimeInMillis(timestamp);

        if (birthDate.after(now)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }

        final int nowYear = now.get(Calendar.YEAR);
        final int birthYear = birthDate.get(Calendar.YEAR);

        int age = nowYear - birthYear;

        final int nowMonth = now.get(Calendar.MONTH);
        final int birthMonth = birthDate.get(Calendar.MONTH);

        if (birthMonth > nowMonth) {
            --age;
        } else if (nowMonth == birthMonth) {
            final int day1 = now.get(Calendar.DAY_OF_MONTH);
            final int day2 = birthDate.get(Calendar.DAY_OF_MONTH);

            if (day2 > day1) {
                --age;
            }
        }

        return age;
    }


}
