package com.aptatek.pkulab.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.main.weekly.chart.CustomYAxisRenderer;
import com.aptatek.pkulab.view.main.weekly.chart.PdfChartDataRenderer;
import com.aptatek.pkulab.view.main.weekly.pdf.PdfEntryData;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.lang.reflect.Field;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PdfExportView extends ConstraintLayout {

    private static final int Y_PADDING = 2;

    @BindView(R.id.textViewTitle)
    TextView tvTitle;
    @BindView(R.id.textViewSubTitle)
    TextView subTitle;
    @BindView(R.id.chart)
    BubbleChart bubbleChart;
    @BindView(R.id.textViewUnitDescription)
    TextView unitDescription;
    @BindView(R.id.textViewLowNumber)
    TextView standardNumber;
    @BindView(R.id.textViewNormalNumber)
    TextView increasedNumber;
    @BindView(R.id.textViewHighNumber)
    TextView highNumber;
    @BindView(R.id.textViewVeryHighNumber)
    TextView veryHighNumber;
    @BindView(R.id.avarage)
    TextView averageText;
    @BindView(R.id.textViewNormalText)
    TextView increasedText;
    @BindView(R.id.textViewLowText)
    TextView standardTextView;
    @BindView(R.id.textViewHighText)
    TextView highTextView;
    @BindView(R.id.textViewVeryHighText)
    TextView veryHighTextView;

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
        standardNumber.setText(getResources().getString(R.string.pdf_export_legend_x, pdfEntryData.getLowCount()));
        increasedNumber.setText(getResources().getString(R.string.pdf_export_legend_x, pdfEntryData.getNormalCount()));
        highNumber.setText(getResources().getString(R.string.pdf_export_legend_x, pdfEntryData.getHighCount()));
        veryHighNumber.setText(getResources().getString(R.string.pdf_export_legend_x, pdfEntryData.getVeryHighCount()));
        averageText.setText(getResources().getString(R.string.pdf_export_average,
                pdfEntryData.getAverageCount(),
                pdfEntryData.getUnit(),
                pdfEntryData.getMin(),
                pdfEntryData.getMax()));
        unitDescription.setText(getResources().getString(R.string.pdf_export_unit_description, pdfEntryData.getUnit()));

        standardTextView.setText(pdfEntryData.getStandardText());
        increasedText.setText(pdfEntryData.getIncreasedText());
        highTextView.setText(pdfEntryData.getHighText());
        veryHighTextView.setText(pdfEntryData.getVeryHighText());

        initChart();

        invalidate();
    }

    private int getAxisMaxForDays(final int input) {
        if (input < 30) {
            return 30;
        }

        return 32;
    }

    // TODO use the same initChart method as WeeklyChartFragment...
    private void initChart() {
        final Typeface typeface = ResourcesCompat.getFont(getContext().getApplicationContext(), R.font.nunito_black);
        final XAxis xAxis = bubbleChart.getXAxis();
        xAxis.setTextColor(getResources().getColor(R.color.applicationSolidGray));
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(final float value, final AxisBase axis) {
                if (Float.compare(value, 0f) <= 0 || Float.compare(value, pdfEntryData.getDaysOfMonth()) > 0) {
                    return "";
                }

                return String.format(Locale.getDefault(), "%.0f", value);
            }
        });
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        final int maxValue = getAxisMaxForDays(pdfEntryData.getDaysOfMonth());
        xAxis.setAxisMaximum(maxValue);
        xAxis.setLabelCount(maxValue, true);

        // need to use reflection since the Axis.setLabelCount sets 25 as max value...
        setLabelCountOnAxisWithReflection(xAxis, maxValue + 1);

        xAxis.setTypeface(typeface);

        final YAxis yAxis = bubbleChart.getAxisLeft();
        final String[] hours = getResources().getStringArray(R.array.weekly_hours);
        yAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getAxisLabel(final float value, final AxisBase axis) {
                final int round = Math.round(value);
                final int index = Math.round(round / (float) Constants.ONE_HOUR_IN_MINUTES);

                return hours[index + Y_PADDING];
            }
        });
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setInverted(true);
        yAxis.setAxisMinimum(-1 * Y_PADDING * Constants.ONE_HOUR_IN_MINUTES);
        yAxis.setAxisMaximum(Constants.ONE_DAY_IN_HOURS * Constants.ONE_HOUR_IN_MINUTES + (Y_PADDING * Constants.ONE_HOUR_IN_MINUTES));
        yAxis.setLabelCount(hours.length, true);
        yAxis.setTextColor(getResources().getColor(R.color.applicationSolidGray));
        yAxis.setTypeface(typeface);

        // need to use reflection since the Axis.setLabelCount sets 25 as max value...
        setLabelCountOnAxisWithReflection(yAxis, hours.length);

        yAxis.setTextColor(getResources().getColor(R.color.applicationSolidGray));
        yAxis.setTypeface(typeface);

        final BubbleData data = new BubbleData();
        data.addDataSet(pdfEntryData.getBubbleDataSet());
        bubbleChart.setRendererLeftYAxis(new CustomYAxisRenderer(bubbleChart));
        bubbleChart.setData(data);
        bubbleChart.getAxisRight().setEnabled(false);
        bubbleChart.getAxisRight().setDrawGridLines(false);
        bubbleChart.setDrawBorders(false);
        bubbleChart.setScaleEnabled(false);
        bubbleChart.getLegend().setEnabled(false);
        bubbleChart.getDescription().setEnabled(false);
        bubbleChart.setRenderer(new PdfChartDataRenderer(bubbleChart));
    }

    private void setLabelCountOnAxisWithReflection(final AxisBase axis, final int count) {
        try {
            final Field mLabelCount = axis.getClass().getSuperclass().getDeclaredField("mLabelCount");
            mLabelCount.setAccessible(true);
            mLabelCount.set(axis, count);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            Timber.d("Failed to set mLabelCount");
        }
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
