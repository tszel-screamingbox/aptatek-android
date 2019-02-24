package com.aptatek.pkulab.domain.model;

import com.google.auto.value.AutoValue;

import java.io.File;

@AutoValue
public abstract class ReportIssue {

    public abstract File getCsvFile();

    public abstract String getDescription();

    public abstract String getTitle();

    public static ReportIssue create(final File csvFile, final String description, final String title) {
        return new AutoValue_ReportIssue(csvFile, description, title);
    }
}
