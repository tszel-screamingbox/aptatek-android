package com.aptatek.pkulab.view.main.weekly.csv;

import android.content.Context;

import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
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

    @Inject
    public CsvExport(final @ApplicationContext Context context) {
        this.context = context;
    }

    public Single<File> generateCsv(final List<TestResult> results) {
        return Single.fromCallable(() -> {
            final File file = new File(context.getFilesDir(), "result.csv");
            final CSVWriter writer = new CSVWriter(new FileWriter(file));

            final List<String[]> data = new ArrayList<>();
            data.add(new String[]{"ID", "Reader", "PKU Level (uMol)", "isFasting", "isSick", "Created At"});

            Ix.from(results).foreach(result -> data.add(new String[]{
                    result.getId(),
                    result.getReaderId(),
                    String.valueOf(result.getPkuLevel().getValue()),
                    String.valueOf(result.isFasting()),
                    String.valueOf(result.isSick()),
                    TimeHelper.timeFromTimestamp(result.getTimestamp()).toString()
            }));

            writer.writeAll(data);
            writer.close();
            return file;
        });
    }
}
