package com.aptatek.pkulab.view.main.weekly.csv;

import android.content.Context;

import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.domain.model.Reminder;
import com.aptatek.pkulab.domain.model.ReminderDay;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.aptatek.pkulab.view.main.weekly.WeeklyChartResourceFormatter;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ix.Ix;
import ix.IxConsumer;

public class CsvExport {

    private final Context context;
    private final WeeklyChartResourceFormatter formatter;
    private final DeviceHelper deviceHelper;

    @Inject
    public CsvExport(final @ApplicationContext Context context,
                     final WeeklyChartResourceFormatter formatter,
                     final DeviceHelper deviceHelper) {
        this.context = context;
        this.formatter = formatter;
        this.deviceHelper = deviceHelper;
    }

    public Single<Attachment> generateAttachment(final List<TestResult> results, final String fileName) {
        return Single.fromCallable(() -> {
            final File file = new File(context.getFilesDir(), fileName);
            final CSVWriter writer = new CSVWriter(new FileWriter(file));

            final List<String[]> data = new ArrayList<>();
            data.add(new String[]{"ID", "Reader ID", "PKU Level (uMol)", "isFasting", "isSick", "Created At"});
            Ix.from(results).foreach(result -> data.add(new String[]{
                    result.getId(),
                    result.getReaderId(),
                    String.valueOf(result.getPkuLevel().getValue()),
                    String.valueOf(result.isFasting()),
                    String.valueOf(result.isSick()),
                    formatter.getFormattedCsvColumn(result.getTimestamp())
            }));

            writer.writeAll(data);
            writer.close();
            final long startDate = results.get(0).getTimestamp();
            final long endDate = results.get(results.size() - 1).getTimestamp();
            final String body = formatter.getFormattedCsvBody(startDate, endDate, deviceHelper.getAppVersion());
            return Attachment.create(file, body);
        });
    }

    public Single<Attachment> generateRemindersAttachment(final List<ReminderDay> reminderDays, final String fileName) {
        return Single.fromCallable(() -> {
            final File file = new File(context.getFilesDir(), fileName);
            final CSVWriter writer = new CSVWriter(new FileWriter(file));

            final List<String[]> data = new ArrayList<>();
            data.add(new String[]{"WeekDay", "Active", "Reminder Hour", "Reminder Minute", "Schedule Type"});
            Ix.from(reminderDays).foreach(reminderDay -> Ix.from(reminderDay.getReminders()).foreach(reminder -> data.add(new String[]{
                    String.valueOf(reminder.getWeekDay()),
                    String.valueOf(reminderDay.isActive()),
                    String.valueOf(reminder.getHour()),
                    String.valueOf(reminder.getMinute()),
                    reminder.getReminderScheduleType().name()})));

            writer.writeAll(data);
            writer.close();
            return Attachment.create(file, "");
        });
    }

    public Single<Attachment> generatePkuRangeInfoAttachment(final PkuRangeInfo pkuRangeInfo, final String fileName) {
        return Single.fromCallable(() -> {
            final File file = new File(context.getFilesDir(), fileName);
            final CSVWriter writer = new CSVWriter(new FileWriter(file));

            final List<String[]> data = new ArrayList<>();
            data.add(new String[]{"Normal Floor Value", "Normal Ceil Value", "High Ceil Value",
                    "Normal Absolute Min Value", "Normal Absolute Min Value", "Pku Level Unit", "Is Default Value"});
            data.add(new String[]{
                    String.valueOf(pkuRangeInfo.getNormalFloorValue()),
                    String.valueOf(pkuRangeInfo.getNormalCeilValue()),
                    String.valueOf(pkuRangeInfo.getHighCeilValue()),
                    String.valueOf(pkuRangeInfo.getNormalAbsoluteMinValue()),
                    String.valueOf(pkuRangeInfo.getNormalAbsoluteMaxValue()),
                    pkuRangeInfo.getPkuLevelUnit().name(),
                    String.valueOf(pkuRangeInfo.isDefaultValue())
            });

            writer.writeAll(data);
            writer.close();
            return Attachment.create(file, "");
        });
    }
}
