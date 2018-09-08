package com.aptatek.pkuapp.view.weekly;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.pkuapp.BuildConfig;
import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.injection.module.chart.ChartModule;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.view.base.BaseActivity;
import com.aptatek.pkuapp.view.weekly.pdf.PdfEntryData;
import com.aptatek.pkuapp.view.weekly.swipe.CustomViewPager;
import com.aptatek.pkuapp.view.weekly.swipe.SwipeAdapter;
import com.aptatek.pkuapp.widget.PdfExportView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

public class WeeklyResultActivity extends BaseActivity<WeeklyResultActivityView, WeeklyResultActivityPresenter> implements WeeklyResultActivityView {

    @Inject
    WeeklyResultActivityPresenter presenter;

    @BindView(R.id.emptyGroup)
    Group emptyGroup;

    @BindView(R.id.viewpager)
    CustomViewPager chartViewPager;

    @BindView(R.id.dateText)
    TextView dateTextView;

    @BindView(R.id.leftArrow)
    ImageView leftArrowImageView;

    @BindView(R.id.rightArrow)
    ImageView rightArrowImageView;

    @BindView(R.id.label)
    TextView tvUnit;

    @BindView(R.id.buttonPdfExport)
    FloatingActionButton pdfExport;

    private SwipeAdapter swipeAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);
        ButterKnife.bind(this);

        // starts with "empty view"
        leftArrowImageView.setVisibility(View.INVISIBLE);
        rightArrowImageView.setVisibility(View.INVISIBLE);

        initAdapter();
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.plus(new RangeInfoModule(), new ChartModule())
                .inject(this);
    }

    @NonNull
    @Override
    public WeeklyResultActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @OnClick(R.id.playIcon)
    public void onPlayButtonClicked() {
        presenter.loadValidWeeks();
        pdfExport.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.leftArrow)
    public void onLeftArrowClicked() {
        final int currentPage = chartViewPager.getCurrentItem();
        presenter.showPage(currentPage - 1);
    }

    @OnClick(R.id.buttonPdfExport)
    public void onPdfExportClicked() {
        presenter.getPdfChartData(chartViewPager.getCurrentItem());
    }

    @OnClick(R.id.rightArrow)
    public void onRightArrowClicked() {
        final int currentPage = chartViewPager.getCurrentItem();
        presenter.showPage(currentPage + 1);
    }

    private void initAdapter() {
        swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), Collections.emptyList());
        chartViewPager.setAdapter(swipeAdapter);
        chartViewPager.disableSwipe(true);
    }

    @OnPageChange(R.id.viewpager)
    public void onPageChanged(final int state) {
        presenter.subTitle(presenter.getValidWeeks().size() - state - 1);
        presenter.updateArrows(state);
    }

    @Override
    public void onSubtitleChanged(final String subtitle) {
        dateTextView.setText(subtitle);
    }

    @Override
    public void onUpdateRightArrow(final boolean isVisible) {
        rightArrowImageView.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onUpdateLeftArrow(final boolean isVisible) {
        leftArrowImageView.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onLoadNextPage(final int page) {
        chartViewPager.setCurrentItem(page, true);
    }

    @Override
    public void displayUnitLabel(final String unitLabel) {
        tvUnit.setText(unitLabel);
    }

    @Override
    public void displayValidWeekList(final List<Integer> validWeeks) {
        emptyGroup.setVisibility(View.GONE);
        swipeAdapter.setData(validWeeks);
        chartViewPager.disableSwipe(false);
        chartViewPager.setCurrentItem(validWeeks.size() - 1);
    }

    @Override
    public void onPdfDataReady(final PdfEntryData pdfData) {
        final PdfExportView content = (PdfExportView) View.inflate(this, R.layout.view_pdf_export, null);
        content.setData(pdfData);

        final PdfDocument document = new PdfDocument();

        final PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                getResources().getDimensionPixelSize(R.dimen.pdf_width),
                getResources().getDimensionPixelSize(R.dimen.pdf_height),
                1).create();

        final PdfDocument.Page page = document.startPage(pageInfo);

        final Canvas canvas = page.getCanvas();
        canvas.save();
        content.draw(canvas);
        canvas.restore();

        document.finishPage(page);

        final File file = new File(getFilesDir(), pdfData.getFileName());

        try {
            final FileOutputStream out = new FileOutputStream(file);
            document.writeTo(out);
            document.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                file));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.pdf_export_email_subject));
        startActivity(Intent.createChooser(emailIntent, ""));
    }
}
