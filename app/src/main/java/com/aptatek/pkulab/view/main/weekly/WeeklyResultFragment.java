package com.aptatek.pkulab.view.main.weekly;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.pkulab.BuildConfig;
import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.MonthPickerDialogModel;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.main.weekly.pdf.PdfEntryData;
import com.aptatek.pkulab.view.main.weekly.swipe.CustomViewPager;
import com.aptatek.pkulab.view.main.weekly.swipe.SwipeAdapter;
import com.aptatek.pkulab.widget.MonthPickerDialog;
import com.aptatek.pkulab.widget.PdfExportView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnPageChange;
import timber.log.Timber;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.view.View.inflate;

public class WeeklyResultFragment extends BaseFragment implements WeeklyResultFragmentView, MonthPickerDialog.MonthPickerDialogCallback {

    private static final String MONTH_PICKER_DIALOG_TAG = "aptatek.month.picker.dialog.tag";

    @Inject
    WeeklyResultFragmentPresenter presenter;

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

    private SwipeAdapter swipeAdapter;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weekly;
    }

    @Override
    protected void initObjects(final View view) {
        // starts with "empty view"
        leftArrowImageView.setVisibility(INVISIBLE);
        rightArrowImageView.setVisibility(INVISIBLE);

        initAdapter();
        presenter.loadValidWeeks();
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.add(new RangeInfoModule(), new ChartModule())
                .inject(this);
    }

    @NonNull
    @Override
    public WeeklyResultFragmentPresenter createPresenter() {
        return presenter;
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

    @OnClick(R.id.dateText)
    public void onDateTextViewClicked() {
        presenter.showMonthPickerDialog();
    }

    private void initAdapter() {
        swipeAdapter = new SwipeAdapter(getBaseActivity().getSupportFragmentManager(), Collections.emptyList());
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
        rightArrowImageView.setVisibility(isVisible ? VISIBLE : INVISIBLE);
    }

    @Override
    public void onUpdateLeftArrow(final boolean isVisible) {
        leftArrowImageView.setVisibility(isVisible ? VISIBLE : INVISIBLE);
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
        swipeAdapter.setData(validWeeks);
        chartViewPager.disableSwipe(false);
        chartViewPager.setCurrentItem(validWeeks.size() - 1);
    }

    @Override
    public void onPdfDataReady(final PdfEntryData pdfData) {
        final PdfExportView content = (PdfExportView) inflate(getContext(), R.layout.view_pdf_export, null);
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

        final File file = new File(getBaseActivity().getFilesDir(), pdfData.getFileName());

        try {
            final FileOutputStream out = new FileOutputStream(file);
            document.writeTo(out);
            document.close();
            out.close();
        } catch (IOException e) {
            Timber.d(e);
        }

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                file));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.pdf_export_email_subject));
        startActivity(Intent.createChooser(emailIntent, ""));
    }

    @Override
    public void showMonthPickerDialog(final MonthPickerDialogModel monthPickerDialogModel) {
        if (requireFragmentManager().findFragmentByTag(MONTH_PICKER_DIALOG_TAG) == null) {
            MonthPickerDialog.create(monthPickerDialogModel, this).show(requireFragmentManager(), MONTH_PICKER_DIALOG_TAG);
        }
    }

    @Override
    public void scrollToItem(final int position) {
        chartViewPager.setCurrentItem(position);
    }

    @Override
    public void onPick(final int year, final int month) {
        presenter.getPageForSelectedMonth(year, month);
    }
}
