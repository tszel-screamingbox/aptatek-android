package com.aptatek.aptatek.view.settings;

import java.util.Objects;
import java.util.UUID;

public class RemindersAdapterItem {
    private String id = UUID.randomUUID().toString();
    private String time;
    private Boolean active;
    private int hour;
    private int minute;

    RemindersAdapterItem(String time, boolean active, int hour, int minute) {
        this.time = time;
        this.active = active;
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemindersAdapterItem that = (RemindersAdapterItem) o;
        return hour == that.hour &&
                minute == that.minute &&
                Objects.equals(id, that.id) &&
                Objects.equals(time, that.time) &&
                Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, time, active, hour, minute);
    }
}
