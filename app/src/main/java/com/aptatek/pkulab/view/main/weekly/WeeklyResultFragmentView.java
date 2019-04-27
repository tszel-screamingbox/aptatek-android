package com.aptatek.pkulab.view.main.weekly;

import com.aptatek.pkulab.domain.model.MonthPickerDialogModel;
import com.aptatek.pkulab.view.main.weekly.csv.Attachment;
import com.aptatek.pkulab.view.main.weekly.pdf.PdfEntryData;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

interface WeeklyResultFragmentView extends MvpView {

    void onSubtitleChanged(final String subtitle);

    void onUpdateRightArrow(final boolean isVisible);

    void onUpdateLeftArrow(final boolean isVisible);

    void onLoadNextPage(final int page);

    void displayUnitLabel(String unitLabel);

    void displayValidWeekList(List<Integer> validWeeks);

    void onPdfDataReady(List<PdfEntryData> data);

    void onCsvDataReady(Attachment attachment);

    void showMonthPickerDialog(MonthPickerDialogModel monthPickerDialogModel);

    void scrollToItem(int position);

    void testNotFound();
}
