package com.aptatek.pkulab.view.main.weekly.csv;

import com.google.auto.value.AutoValue;

import java.io.File;

@AutoValue
public abstract class Attachment {

    public abstract File getCsvFile();

    public abstract String getBody();

    public static Attachment create(final File csvFile, final String body) {
        return new AutoValue_Attachment(csvFile, body);
    }
}
