package com.aptatek.pkulab.view.parentalgate.welcome;

import android.os.Parcelable;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AgeVerificationResult implements Parcelable {

    @DrawableRes
    public abstract int getIconRes();

    @NonNull
    public abstract String getTitle();

    @NonNull
    public abstract String getMessage();

    public abstract boolean isShowButton();

    public static Builder builder() {
        return new $AutoValue_AgeVerificationResult.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setIconRes(@DrawableRes int iconRes);

        public abstract Builder setTitle(@NonNull String title);

        public abstract Builder setMessage(@NonNull String message);

        public abstract Builder setShowButton(boolean showButton);

        public abstract AgeVerificationResult build();

    }

}
