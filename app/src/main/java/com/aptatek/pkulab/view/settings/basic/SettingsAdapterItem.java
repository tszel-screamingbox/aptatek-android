package com.aptatek.pkulab.view.settings.basic;

import com.aptatek.pkulab.view.base.AdapterItem;

import java.util.Objects;

public class SettingsAdapterItem implements AdapterItem {

    private final SettingsItem settingsItem;
    private final boolean enabled;
    private final boolean checked;

    public SettingsAdapterItem(final SettingsItem settingsItem, final boolean enabled, final boolean checked) {
        this.settingsItem = settingsItem;
        this.enabled = enabled;
        this.checked = checked;
    }

    public SettingsItem getSettingsItem() {
        return settingsItem;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    public Object uniqueIdentifier() {
        return settingsItem.ordinal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettingsAdapterItem that = (SettingsAdapterItem) o;
        return enabled == that.enabled &&
                checked == that.checked &&
                settingsItem == that.settingsItem;
    }

    @Override
    public int hashCode() {

        return Objects.hash(settingsItem, enabled, checked);
    }

    @Override
    public String toString() {
        return "SettingsAdapterItem{" +
                "settingsItem=" + settingsItem +
                ", enabled=" + enabled +
                ", checked=" + checked +
                '}';
    }
}
