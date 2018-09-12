package com.aptatek.pkuapp.view.test.base;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.model.AlertDialogModel;
import com.aptatek.pkuapp.injection.component.FragmentComponent;
import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.view.base.BaseFragment;
import com.aptatek.pkuapp.view.dialog.AlertDialogDecisionListener;
import com.aptatek.pkuapp.view.dialog.AlertDialogFragment;
import com.aptatek.pkuapp.view.test.TestActivityCommonView;
import com.aptatek.pkuapp.view.test.TestScreens;

import butterknife.BindView;

public abstract class TestBaseFragment<V extends TestFragmentBaseView, P extends TestBasePresenter<V>> extends BaseFragment<V, P>
        implements TestFragmentBaseView {

    private  static final String TAG_ALERT = "alert";

    private TestFragmentComponent testFragmentComponent;

    @BindView(R.id.testTitle)
    protected TextView tvTitle;
    @BindView(R.id.testMessage)
    protected TextView tvMessage;
    @BindView(R.id.testAlertText)
    @Nullable
    protected TextView tvAlert;
    @Nullable
    @BindView(R.id.testVideo)
    protected VideoView videoView;

    @Override
    protected void injectFragment(@NonNull final FragmentComponent fragmentComponent) {
        if (testFragmentComponent == null) {
            testFragmentComponent = fragmentComponent.plus(new TestModule());
        }
        injectTestFragment(testFragmentComponent);
    }

    protected abstract void injectTestFragment(TestFragmentComponent fragmentComponent);

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setTitle(@NonNull final String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setMessage(@NonNull final String message) {
        tvMessage.setText(message);
    }

    @Override
    public void setDisclaimerViewVisible(final boolean visible) {
        if (tvAlert != null) {
            tvAlert.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setDisclaimerMessage(@NonNull final String message) {
        if (tvAlert != null) {
            tvAlert.setText(message);
        }
    }

    @Override
    public void showAlertDialog(@NonNull final AlertDialogModel alertDialogModel, @Nullable final AlertDialogDecisionListener listener) {
        final AlertDialogFragment alertDialogFragment = AlertDialogFragment.create(alertDialogModel, listener);
        alertDialogFragment.show(getChildFragmentManager(), TAG_ALERT);
    }


    @Override
    public void playVideo(@NonNull final Uri uri, final boolean shouldLoop) {
        if (videoView != null) {
            if (videoView.isPlaying()) {
                videoView.stopPlayback();
            }

            videoView.setVideoURI(uri);
            videoView.setOnPreparedListener(mp -> {
                mp.setLooping(shouldLoop);
                mp.start();
            });
        }
    }

    @Override
    public void setBottomBarVisible(final boolean visible) {
        runOnTestTestActivityView(view -> view.setBottomBarVisible(visible));
    }

    @Override
    public void setBatteryIndicatorVisible(final boolean visible) {
        runOnTestTestActivityView(view -> view.setBatteryIndicatorVisible(visible));
    }

    @Override
    public void setBatteryPercentageText(@NonNull final String percentageText) {
        runOnTestTestActivityView(view -> view.setBatteryPercentageText(percentageText));
    }

    @Override
    public void setProgressVisible(final boolean visible) {
        runOnTestTestActivityView(view -> view.setProgressVisible(visible));
    }

    @Override
    public void setProgressPercentage(final int percentage) {
        runOnTestTestActivityView(view -> view.setProgressPercentage(percentage));
    }

    @Override
    public void showNextScreen() {
        runOnTestTestActivityView(TestActivityCommonView::showNextScreen);
    }

    @Override
    public void showPreviousScreen() {
        runOnTestTestActivityView(TestActivityCommonView::showPreviousScreen);
    }

    @Override
    public boolean onNextPressed() {
        return false;
    }

    @Override
    protected void initObjects(final View view) {
        presenter.initWithDefaults();
    }

    @Override
    public void onStop() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.stopPlayback();
            videoView.setOnPreparedListener(null);
        }

        super.onStop();
    }

    private void runOnTestTestActivityView(final TestActivityViewAction action) {
        if (getActivity() instanceof TestActivityCommonView) {
            action.run((TestActivityCommonView) getActivity());
        }
    }

    private interface TestActivityViewAction {
        void run(TestActivityCommonView view);
    }

    public abstract TestScreens getScreen();
}
