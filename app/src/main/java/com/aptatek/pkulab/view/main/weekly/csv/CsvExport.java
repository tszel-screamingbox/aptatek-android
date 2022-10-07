package com.aptatek.pkulab.view.main.weekly.csv;

import android.content.Context;
import android.text.TextUtils;

import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
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
            data.add(new String[]{"ID", "Reader ID", "MAC", "Numeric value", "Units", "Text result", "Timestamp", "End Timestamp", "Valid", "Overall result", "Assay", "Assay version", "Temperature", "Humidity", "HW version", "SW version", "FW version", "Mode", "CassetteLot", "CassetteExpiry", "Config#", "Assay#", "Raw"});
            Ix.from(results).foreach(result -> {
                    final PkuLevel level = result.getPkuLevel() == null ? PkuLevel.create(-1f, PkuLevelUnits.MABS) : result.getPkuLevel();
                    data.add(new String[]{
                            result.getId(),
                            result.getReaderId(),
                            result.getReaderMac(),
                            String.valueOf(level.getValue()),
                            level.getUnit().name(),
                            TextUtils.isEmpty(level.getTextResult()) ? "N/A" : level.getTextResult(),
                            formatter.getFormattedCsvColumn(result.getTimestamp()),
                            formatter.getFormattedCsvColumn(result.getEndTimestamp()),
                            result.isValid() ? "Y" : "N",
                            TextUtils.isEmpty(result.getOverallResult()) ? "N/A" : result.getOverallResult(),
                            TextUtils.isEmpty(result.getAssay()) ? "N/A" : result.getAssay(),
                            String.valueOf(result.getAssayVersion()),
                            TextUtils.isEmpty(result.getTemperature()) ? "N/A" : result.getTemperature(),
                            TextUtils.isEmpty(result.getHumidity()) ? "N/A" : result.getHumidity(),
                            TextUtils.isEmpty(result.getHardwareVersion()) ? "N/A" : result.getHardwareVersion(),
                            TextUtils.isEmpty(result.getSoftwareVersion()) ? "N/A" : result.getSoftwareVersion(),
                            TextUtils.isEmpty(result.getFirmwareVersion()) ? "N/A" : result.getFirmwareVersion(),
                            TextUtils.isEmpty(result.getReaderMode()) ? "N/A" : result.getReaderMode(),
                            String.valueOf(result.getCassetteLot()),
                            String.valueOf(result.getCassetteExpiry()),
                            TextUtils.isEmpty(result.getConfigHash()) ? "N/A" : result.getConfigHash(),
                            TextUtils.isEmpty(result.getAssayHash()) ? "N/A" : result.getAssayHash(),
                            result.getRawResponse()
                    });
            });

            writer.writeAll(data);
            writer.close();

            final long startDate = results.isEmpty() ? 0L : results.get(0).getTimestamp();
            final long endDate = results.isEmpty() ? 0L : results.get(results.size() - 1).getTimestamp();
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
