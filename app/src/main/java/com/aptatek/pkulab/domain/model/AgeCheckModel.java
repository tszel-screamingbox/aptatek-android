package com.aptatek.pkulab.domain.model;


import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AgeCheckModel {

    public abstract long getBirthDate();

    @Nullable
    public abstract String getBirthDateFormatted();

    public abstract int getAge();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_AgeCheckModel.Builder()
                .setBirthDate(0L)
                .setAge(0);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setBirthDate(long birthDate);

        public abstract Builder setBirthDateFormatted(@Nullable String birthDateFormatted);

        public abstract Builder setAge(int age);

        public abstract AgeCheckModel build();

    }

}
