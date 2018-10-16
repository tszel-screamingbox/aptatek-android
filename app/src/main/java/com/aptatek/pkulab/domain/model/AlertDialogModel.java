package com.aptatek.pkulab.domain.model;

import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import com.aptatek.pkulab.R;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AlertDialogModel implements Parcelable {

    @Nullable
    public abstract String getTitle();

    @Nullable
    public abstract String getMessage();

    @Nullable
    public abstract String getPositiveButtonText();

    @Nullable
    public abstract String getNegativeButtonText();

    @Nullable
    public abstract String getNeutralButtonText();

    @ColorRes
    public abstract int getPositiveButtonTextColor();

    @ColorRes
    public abstract int getNegativeButtonTextColor();

    @ColorRes
    public abstract int getNeutralButtonTextColor();

    @StyleRes
    public abstract int getTheme();

    public abstract boolean isCancelable();

    public static Builder builder() {
        return new AutoValue_AlertDialogModel.Builder()
                .setTheme(R.style.DefaultDialogTheme)
                .setNegativeButtonTextColor(R.color.dialButtonGrey)
                .setPositiveButtonTextColor(R.color.dialButtonGrey)
                .setNeutralButtonTextColor(R.color.dialButtonGrey);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setTitle(@NonNull String title);

        public abstract Builder setPositiveButtonTextColor(@ColorRes int positiveButtonTextColor);

        public abstract Builder setNegativeButtonTextColor(@ColorRes int negativeButtonTextColor);

        public abstract Builder setNeutralButtonTextColor(@ColorRes int neutralButtonTextColor);

        public abstract Builder setMessage(@NonNull String message);

        public abstract Builder setPositiveButtonText(@NonNull String positiveButtonText);

        public abstract Builder setNegativeButtonText(@NonNull String negativeButtonText);

        public abstract Builder setNeutralButtonText(@NonNull String neutralButtonText);

        public abstract Builder setTheme(@StyleRes int theme);

        public abstract Builder setCancelable(boolean cancelable);

        public abstract AlertDialogModel build();
    }
}
