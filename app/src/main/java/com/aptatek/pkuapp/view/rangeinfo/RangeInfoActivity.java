package com.aptatek.pkuapp.view.rangeinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.view.base.BaseActivity;
import com.aptatek.pkuapp.view.settings.pkulevel.RangeSettingsActivity;
import com.aptatek.pkuapp.widget.RangeInfoRowView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RangeInfoActivity extends BaseActivity<RangeInfoView, RangeInfoPresenter> implements RangeInfoView {

    public static Intent starter(@NonNull final Context context) {
        return new Intent(context, RangeInfoActivity.class);
    }

    @BindView(R.id.rangeinfo_very_high)
    RangeInfoRowView tvVeryHigh;
    @BindView(R.id.rangeinfo_high)
    RangeInfoRowView tvHigh;
    @BindView(R.id.rangeinfo_normal)
    RangeInfoRowView tvNormal;
    @BindView(R.id.rangeinfo_low)
    RangeInfoRowView tvLow;
    @BindView(R.id.rangeinfo_units)
    TextView tvUnits;

    @Inject
    RangeInfoPresenter presenter;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_info);
        ButterKnife.bind(this);
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
        tvVeryHigh.setRange(uiModel.getVeryHighValue(), getString(R.string.rangeinfo_very_high));
        tvHigh.setRange(uiModel.getHighValue(), getString(R.string.rangeinfo_high));
        tvNormal.setRange(uiModel.getNormalValue(), getString(R.string.rangeinfo_normal));
        tvLow.setRange(uiModel.getLowValue(), getString(R.string.rangeinfo_low));
        tvUnits.setText(uiModel.getUnitValue());
    }

    @NonNull
    @Override
    public RangeInfoPresenter createPresenter() {
        return presenter;
    }

    @OnClick(R.id.rangeinfo_edit)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.rangeinfo_edit)
    public void onClickEdit() {
        launchActivity(RangeSettingsActivity.starter(this), false, Animation.FADE);
    }
}
