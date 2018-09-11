package com.aptatek.pkuapp.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.util.Constants;
import com.aptatek.pkuapp.view.weekly.chart.PdfChartDataRenderer;
import com.aptatek.pkuapp.view.weekly.pdf.PdfEntryData;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PdfExportView extends ConstraintLayout {

    @BindView(R.id.textViewTitle)
    TextView tvTitle;
    @BindView(R.id.textViewSubTitle)
    TextView subTitle;
    @BindView(R.id.chart)
    BubbleChart bubbleChart;
    @BindView(R.id.textViewUnitDescription)
    TextView unitDescription;
    @BindView(R.id.textViewFastingNumber)
    TextView fastingNumber;
    @BindView(R.id.textViewSickNumber)
    TextView sickNumber;
    @BindView(R.id.textViewAverageNumber)
    TextView averageNumber;
    @BindView(R.id.textViewLowNumber)
    TextView lowNumber;
    @BindView(R.id.textViewNormalNumber)
    TextView normalNumber;
    @BindView(R.id.textViewHighNumber)
    TextView highNumber;
    @BindView(R.id.textViewVeryHighNumber)
    TextView veryHighNumber;
    @BindView(R.id.textViewAverageText)
    TextView averageText;

    private PdfEntryData pdfEntryData;

    public PdfExportView(final Context context) {
        this(context, null, 0);
    }

    public PdfExportView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PdfExportView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(final @NonNull PdfEntryData pdfEntryData) {
        this.pdfEntryData = pdfEntryData;

        subTitle.setText(pdfEntryData.getFormattedDate());
        lowNumber.setText(getResources().getString(R.string.pdf_export_legend_x, pdfEntryData.getLowCount()));
        normalNumber.setText(getResources().getString(R.string.pdf_export_legend_x, pdfEntryData.getNormalCount()));
        highNumber.setText(getResources().getString(R.string.pdf_export_legend_x, pdfEntryData.getHighCount()));
        veryHighNumber.setText(getResources().getString(R.string.pdf_export_legend_x, pdfEntryData.getVeryHighCount()));
        sickNumber.setText(getResources().getString(R.string.pdf_export_legend_x, pdfEntryData.getSickCount()));
        averageNumber.setText(String.valueOf(pdfEntryData.getAverageCount()));
        fastingNumber.setText(getResources().getString(R.string.pdf_export_legend_x, pdfEntryData.getFastingCount()));
        averageText.setText(getResources().getString(R.string.pdf_export_average, String.format(Locale.getDefault(), "%.2f", pdfEntryData.getDeviation())));
        unitDescription.setText(getResources().getString(R.string.pdf_export_unit_description, pdfEntryData.getUnit()));

        initChart();

        invalidate();
    }

    private int getNextEvenFor(final int input) {
        if (input % 2 == 0) {
            return input;
        }

        return input + 1;
    }

    private void initChart() {
        final XAxis xAxis = bubbleChart.getXAxis();
        xAxis.setTextColor(getResources().getColor(R.color.applicationLightGray));
        xAxis.setValueFormatter((value, axis) -> {
            if (Float.compare(value, 0f) <= 0 || Float.compare(value, pdfEntryData.getDaysOfMonth()) > 0) {
                return "";
            }

            return String.valueOf(String.format(Locale.getDefault(), "%.0f", value));
        });
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        final int maxValue = getNextEvenFor(pdfEntryData.getDaysOfMonth());
        xAxis.setAxisMaximum(maxValue);
        xAxis.setLabelCount((maxValue / 2) + 1, true);

        final YAxis yAxis = bubbleChart.getAxisLeft();
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
        data.addDataSet(pdfEntryData.getBubbleDataSet());
        bubbleChart.setData(data);
        bubbleChart.getAxisRight().setEnabled(false);
        bubbleChart.getAxisRight().setDrawGridLines(false);
        bubbleChart.setDrawBorders(false);
        bubbleChart.setScaleEnabled(false);
        bubbleChart.getLegend().setEnabled(false);
        bubbleChart.getDescription().setEnabled(false);
        bubbleChart.setRenderer(new PdfChartDataRenderer(bubbleChart));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        final int measureWidth =
                View.MeasureSpec.makeMeasureSpec(getContext().getResources().getDimensionPixelSize(R.dimen.pdf_width), View.MeasureSpec.EXACTLY);
        final int measuredHeight =
                View.MeasureSpec.makeMeasureSpec(getContext().getResources().getDimensionPixelSize(R.dimen.pdf_height), View.MeasureSpec.EXACTLY);

        measure(measureWidth, measuredHeight);

        layout(0, 0, measureWidth * 2, measuredHeight * 2);
    }
}
