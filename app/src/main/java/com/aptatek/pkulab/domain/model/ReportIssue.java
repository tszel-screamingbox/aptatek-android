package com.aptatek.pkulab.domain.model;

import com.aptatek.pkulab.view.main.weekly.csv.Attachment;
import com.google.auto.value.AutoValue;

import java.io.File;
import java.util.List;

@AutoValue
public abstract class ReportIssue {

    public abstract List<Attachment> getAttachments();

    public abstract String getDescription();

    public abstract String getTitle();

    public abstract String getTargetEmail();

    public static ReportIssue create(final List<Attachment> attachments, final String description, final String title, final String targetEmail) {
        return new AutoValue_ReportIssue(attachments, description, title, targetEmail);
    }
}
