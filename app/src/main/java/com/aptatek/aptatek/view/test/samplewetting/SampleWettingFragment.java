package com.aptatek.aptatek.view.test.samplewetting;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.test.TestFragmentComponent;
import com.aptatek.aptatek.view.rangeinfo.RangeInfoActivity;
import com.aptatek.aptatek.view.test.TestScreens;
import com.aptatek.aptatek.view.test.base.TestBaseFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class SampleWettingFragment extends TestBaseFragment<SampleWettingView, SampleWettingPresenter>
    implements SampleWettingView {

    @BindView(R.id.wettingCountdown)
    TextView countdown;

    @BindView(R.id.wettingImage)
    ImageView image;

    @Inject
    SampleWettingPresenter sampleWettingPresenter;

    @Override
    protected void initObjects(final View view) {
        super.initObjects(view);
        presenter.initUi();
    }

    @Override
    protected void injectTestFragment(@NonNull final TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @NonNull
    @Override
    public SampleWettingPresenter createPresenter() {
        return sampleWettingPresenter;
    }

    @Override
    public boolean onNavigateBackPressed() {
        showScreen(TestScreens.CANCEL);

        return true;
    }

    @Override
    public boolean onBackPressed() {
        showScreen(TestScreens.CANCEL);

        return true;
    }

    @Override
    public void navigateForward() {
        super.navigateForward();

        getActivity().startActivity(RangeInfoActivity.starter(getActivity()));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_sample_wetting;
    }

    @Override
    public void showCountdown(@NonNull final String countdownRemaining) {
        countdown.setText(countdownRemaining);
    }

    @Override
    public void showImage(@DrawableRes final int imageRes) {
        image.setImageResource(imageRes);
    }
}
