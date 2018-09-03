package com.aptatek.pkuapp.view.weekly.chart;

import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.FragmentComponent;
import com.aptatek.pkuapp.injection.module.chart.ChartModule;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.util.Constants;
import com.aptatek.pkuapp.view.base.BaseFragment;
import com.aptatek.pkuapp.view.weekly.pdf.PdfEntryData;
import com.aptatek.pkuapp.widget.PdfExportView;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import butterknife.BindView;


public class WeeklyChartFragment extends BaseFragment implements WeeklyChartView {

    private static final float MIN_X = -0.5f;
    private static final float MAX_X = 6.5f;

    @Arg
    int weekBefore;

    @Inject
    WeeklyChartPresenter presenter;

    @BindView(R.id.weeklyChart)
    BubbleChart weeklyBubbleChart;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weekly_chart;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this, savedInstanceState);
    }

    @Override
    protected void initObjects(final View view) {
        initChart();
        if (weekBefore >= 0) {
            presenter.getChartData(weekBefore);

            // TODO Robi do it on FAB press as the design says
            presenter.getPdfChartData(weekBefore);
        }
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.plus(new RangeInfoModule(), new ChartModule())
                .inject(this);
    }

    @NonNull
    @Override
    public WeeklyChartPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void displayChartData(final BubbleDataSet bubbleDataSet) {
        weeklyBubbleChart.getData().addDataSet(bubbleDataSet);
        weeklyBubbleChart.invalidate();
    }

    @Override
    public void onPdfDataReady(final PdfEntryData pdfData) {
        PdfExportView content = (PdfExportView) View.inflate(requireContext(), R.layout.view_pdf_export, null);
        content.setData(pdfData);

        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                getResources().getDimensionPixelSize(R.dimen.pdf_width),
                getResources().getDimensionPixelSize(R.dimen.pdf_height),
                1).create();

        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        canvas.save();
        content.draw(canvas);
        canvas.restore();

        document.finishPage(page);

        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, "aptatek.pdf");

        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();
    }

    private void initChart() {
        final XAxis xAxis = weeklyBubbleChart.getXAxis();
        final String[] days = getResources().getStringArray(R.array.weekly_days);
        xAxis.setTextColor(getResources().getColor(R.color.applicationLightGray));
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(MIN_X);
        xAxis.setAxisMaximum(MAX_X);

        final YAxis yAxis = weeklyBubbleChart.getAxisLeft();
        final String[] hours = getResources().getStringArray(R.array.weekly_hours);
        yAxis.setValueFormatter((value, axis) -> {
            final int round = Math.round(value);
            final int index = Math.round(round / (float) Constants.ONE_HOUR_IN_MINUTES);

            return hours[index];
        });
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setInverted(true);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(Constants.ONE_DAY_IN_HOURS * Constants.ONE_HOUR_IN_MINUTES);
        yAxis.setLabelCount(hours.length, true);
        yAxis.setTextColor(getResources().getColor(R.color.applicationLightGray));

        final BubbleData data = new BubbleData();
        weeklyBubbleChart.setData(data);
        weeklyBubbleChart.getAxisRight().setEnabled(false);
        weeklyBubbleChart.getAxisRight().setDrawGridLines(false);
        weeklyBubbleChart.setDrawBorders(false);
        weeklyBubbleChart.setScaleEnabled(false);
        weeklyBubbleChart.getLegend().setEnabled(false);
        weeklyBubbleChart.getDescription().setEnabled(false);
        weeklyBubbleChart.setRenderer(new CustomBubbleChartRenderer(weeklyBubbleChart));
    }

}
