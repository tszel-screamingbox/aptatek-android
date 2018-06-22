package com.aptatek.aptatek.view.settings.reminder;

import java.util.Objects;

// TODO refactor to IMMUTABLE. Use AutoValue
public class RemindersAdapterItem {
    private String id;
    private String time;
    private Boolean active;
    private int hour;
    private int minute;

    public RemindersAdapterItem(final String id,final String time, final boolean active,final int hour,final int minute) {
        this.id = id;
        this.time = time;
        this.active = active;
        this.hour = hour;
        this.minute = minute;
    }

    public String getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(final int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(final int minute) {
        this.minute = minute;
    }

    public String getTime() {
        return time;
    }

    public void setTime(final String time) {
        this.time = time;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RemindersAdapterItem that = (RemindersAdapterItem) o;
        return hour == that.hour
                && minute == that.minute
                && Objects.equals(id, that.id)
                && Objects.equals(time, that.time)
                && Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, active, hour, minute);
    }
}
