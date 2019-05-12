package com.aptatek.pkulab.view.main.weekly.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.renderer.BubbleChartRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;

import java.util.List;

import static com.github.mikephil.charting.utils.Utils.calcTextHeight;
import static com.github.mikephil.charting.utils.Utils.convertDpToPixel;
import static com.github.mikephil.charting.utils.Utils.drawImage;

// Simply copied the BubbleChartRenderer's source.
// The difference are in this implementation:
// - in the drawDataSet method since we need custom bubble colors for each entry.
// - drawHighlighted is empty since we don't need highlighting
// - drawValues modified to render text properly positioned
public class PdfChartDataRenderer extends BubbleChartRenderer {

    private static final float BUBBLE_TEXT_PADDING_RATIO = 0.1f;
    private static final float BUBBLE_TEXT_SIZE_STEP = 1f;
    private static final String DEMO_TEXT_4_DIGITS = "9999";
    private static final float CIRCLE_BORDER = 1.25f;
    private static final float DASHED_LINE_LENGTH = 3f;


    protected BubbleDataProvider mChart;

    public PdfChartDataRenderer(final BubbleChart chart) {
        super(chart, chart.getAnimator(), chart.getViewPortHandler());
        mChart = chart;

        mRenderPaint.setStyle(Paint.Style.FILL);

        mHighlightPaint.setStyle(Paint.Style.STROKE);
        mHighlightPaint.setStrokeWidth(convertDpToPixel(1.5f));
    }

    @Override
    public void initBuffers() {
        // no-op
    }

    @Override
    public void drawData(Canvas c) {

        BubbleData bubbleData = mChart.getBubbleData();

        for (IBubbleDataSet set : bubbleData.getDataSets()) {

            if (set.isVisible())
                drawDataSet(c, set);
        }
    }

    private float[] sizeBuffer = new float[4];
    private float[] pointBuffer = new float[2];

    protected float getShapeSize(float entrySize, float reference) {
        return entrySize * reference;
    }

    private float getReferenceSize() {
        // calculate the full width of 1 step on the x-axis
        final float maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0]);
        final float maxBubbleHeight = Math.abs(mViewPortHandler.contentBottom() - mViewPortHandler.contentTop());
        return Math.min(maxBubbleHeight, maxBubbleWidth);
    }

    protected void drawDataSet(final Canvas c, final IBubbleDataSet dataSet) {

        final Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
        final float phaseY = mAnimator.getPhaseY();
        final DashPathEffect effect = new DashPathEffect(new float[]{convertDpToPixel(DASHED_LINE_LENGTH * 2), convertDpToPixel(DASHED_LINE_LENGTH)}, 0);
        final Paint white = new Paint();
        white.setColor(Color.WHITE);

        mXBounds.set(mChart, dataSet);

        sizeBuffer[0] = 0f;
        sizeBuffer[2] = 1f;

        trans.pointValuesToPixel(sizeBuffer);

        final float referenceSize = getReferenceSize();

        for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {

            final BubbleEntry entry = dataSet.getEntryForIndex(j);

            pointBuffer[0] = entry.getX();
            pointBuffer[1] = (entry.getY()) * phaseY;
            trans.pointValuesToPixel(pointBuffer);

            final float shapeHalf = getShapeSize(entry.getSize(), referenceSize) / 2f;

            if (!mViewPortHandler.isInBoundsTop(pointBuffer[1] + shapeHalf)
                    || !mViewPortHandler.isInBoundsBottom(pointBuffer[1] - shapeHalf))
                continue;

            if (!mViewPortHandler.isInBoundsLeft(pointBuffer[0] + shapeHalf))
                continue;

            if (!mViewPortHandler.isInBoundsRight(pointBuffer[0] - shapeHalf))
                break;

            final int color;
            final int labelColor;
            final Object data = entry.getData();
            boolean isSick = false;
            boolean isFasting = false;
            if (data instanceof ChartEntryData) {
                final ChartEntryData chartEntryData = (ChartEntryData) data;
                color = chartEntryData.getBubbleColor();
                isFasting = chartEntryData.isFasting();
                isSick = chartEntryData.isSick();
                labelColor = chartEntryData.getLabelColor();
            } else {
                color = dataSet.getColor((int) entry.getX());
                labelColor = color;
            }

            mRenderPaint.setColor(color);
            c.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mRenderPaint);

            if (isSick) {
                c.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf - convertDpToPixel(CIRCLE_BORDER), white);
                c.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf - convertDpToPixel(CIRCLE_BORDER) * 2, mRenderPaint);
            }

            if (isFasting) {
                mHighlightPaint.setColor(labelColor);
                mHighlightPaint.setStyle(Paint.Style.STROKE);
                mHighlightPaint.setPathEffect(effect);
                c.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mHighlightPaint);
            }
        }
    }

    @Override
    public void drawValues(Canvas c) {

        BubbleData bubbleData = mChart.getBubbleData();

        if (bubbleData == null)
            return;

        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {

            final List<IBubbleDataSet> dataSets = bubbleData.getDataSets();
            final float referenceSize = getReferenceSize();

            for (int i = 0; i < dataSets.size(); i++) {

                IBubbleDataSet dataSet = dataSets.get(i);

                if (!shouldDrawValues(dataSet))
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                final float phaseY = mAnimator.getPhaseY();

                mXBounds.set(mChart, dataSet);

                final float[] positions = mChart.getTransformer(dataSet.getAxisDependency())
                        .generateTransformedValuesBubble(dataSet, phaseY, mXBounds.min, mXBounds.max);

                final ValueFormatter formatter = dataSet.getValueFormatter();

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = convertDpToPixel(iconsOffset.x);
                iconsOffset.y = convertDpToPixel(iconsOffset.y);

                for (int j = 0; j < positions.length; j += 2) {

                    float x = positions[j];
                    float y = positions[j + 1];

                    if (!mViewPortHandler.isInBoundsRight(x))
                        break;

                    if ((!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y)))
                        continue;

                    BubbleEntry entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min);
                    final ChartEntryData data = (ChartEntryData) entry.getData();

                    final int valueTextColor = data.getLabelColor();
                    final float shapeSize = getShapeSize(entry.getSize(), referenceSize);
                    mValuePaint.setTextSize(findOptimalTextSize(shapeSize, DEMO_TEXT_4_DIGITS, mValuePaint.getTextSize()));

                    if (dataSet.isDrawValuesEnabled()) {
                        final float lineHeight = calcTextHeight(mValuePaint, DEMO_TEXT_4_DIGITS);
                        drawValue(c, formatter.getBubbleLabel(entry), x, y + (0.5f * lineHeight), valueTextColor);
                    }

                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                        Drawable icon = entry.getIcon();

                        drawImage(
                                c,
                                icon,
                                (int) (x + iconsOffset.x),
                                (int) (y + iconsOffset.y),
                                icon.getIntrinsicWidth(),
                                icon.getIntrinsicHeight());
                    }
                }

                MPPointF.recycleInstance(iconsOffset);
            }
        }
    }

    private float findOptimalTextSize(final float maxSize, final String text, final float currentTextSize) {
        final TextPaint textPaint = new TextPaint(mValuePaint);
        textPaint.setTextSize(currentTextSize);
        final float measuredWidth = textPaint.measureText(text);

        final float padding = maxSize * BUBBLE_TEXT_PADDING_RATIO;
        if (measuredWidth > maxSize - padding) {
            final float reducedTextSize = currentTextSize - BUBBLE_TEXT_SIZE_STEP;
            return findOptimalTextSize(maxSize, text, reducedTextSize);
        }

        return currentTextSize;
    }

    @Override
    public void drawExtras(Canvas c) {
        // do nothing
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {
        // do nothing
    }

}
