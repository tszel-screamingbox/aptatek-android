package com.aptatek.pkulab.view.main.weekly.csv;

import android.content.Context;

import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.aptatek.pkulab.view.main.weekly.WeeklyChartDateFormatter;
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
    private final WeeklyChartDateFormatter formatter;
    private final DeviceHelper deviceHelper;

    @Inject
    public CsvExport(final @ApplicationContext Context context,
                     final WeeklyChartDateFormatter formatter,
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
}
