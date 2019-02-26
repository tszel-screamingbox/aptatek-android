package com.aptatek.pkulab.view.settings.basic;

import android.support.annotation.StringRes;

import com.aptatek.pkulab.R;

public enum SettingsItem {

    FINGERPRINT_AUTH(0, 0),
    DAILY_REMINDERS(R.string.settings_daily_reminders, R.string.settings_daily_reminder_description),
    PHE_PREFERENCES(R.string.settings_units_title, R.string.settings_units_description),
    HELP(R.string.settings_help, 0),
    PRIVACY_POLICY(R.string.settings_privacy, 0),
    TERMS_AND_CONDITIONS(R.string.settings_terms, 0);

    private @StringRes
    int titleRes;
    private @StringRes
    int descriptionRes;

    SettingsItem(final int titleRes, final int descriptionRes) {
        this.titleRes = titleRes;
        this.descriptionRes = descriptionRes;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public int getDescriptionRes() {
        return descriptionRes;
    }

}
