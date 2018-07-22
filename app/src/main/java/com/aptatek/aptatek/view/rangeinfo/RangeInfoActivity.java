package com.aptatek.aptatek.view.rangeinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.settings.pkulevel.RangeSettingsActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RangeInfoActivity extends BaseActivity<RangeInfoView, RangeInfoPresenter> implements RangeInfoView {

    public static Intent starter(@NonNull final Context context) {
        return new Intent(context, RangeInfoActivity.class);
    }

    @BindView(R.id.rangeinfo_veryhigh)
    TextView tvVeryHigh;
    @BindView(R.id.rangeinfo_high)
    TextView tvHigh;
    @BindView(R.id.rangeinfo_normal)
    TextView tvNormal;
    @BindView(R.id.rangeinfo_low)
    TextView tvLow;
    @BindView(R.id.rangeinfo_units)
    TextView tvUnits;
    @BindView(R.id.rangeinfo_edit)
    TextView tvEdit;

    @Inject
    RangeInfoPresenter presenter;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_info);
        ButterKnife.bind(this);

        final String text = getString(R.string.rangeinfo_edit_level_preferences);
        final SpannableString editRangesText = new SpannableString(text);
        editRangesText.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        tvEdit.setText(editRangesText);
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.refresh();
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.plus(new RangeInfoModule())
                .inject(this);
    }

    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @Override
    public void displayRangeInfo(@NonNull final RangeInfoUiModel uiModel) {
        tvVeryHigh.setText(uiModel.getVeryHighValue());
        tvHigh.setText(uiModel.getHighValue());
        tvNormal.setText(uiModel.getNormalValue());
        tvLow.setText(uiModel.getLowValue());
        tvUnits.setText(uiModel.getUnitValue());
    }

    @NonNull
    @Override
    public RangeInfoPresenter createPresenter() {
        return presenter;
    }

    @OnClick(R.id.rangeinfo_back)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.rangeinfo_edit)
    public void onClickEdit() {
        launchActivity(RangeSettingsActivity.starter(this), false, Animation.FADE);
    }
}
