package com.aptatek.pkulab.view.test.base;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisionListener;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.test.TestActivityCommonView;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.widget.HeaderView;
import com.mklimek.frameviedoview.FrameVideoView;
import com.mklimek.frameviedoview.FrameVideoViewListener;

import butterknife.BindView;

public abstract class TestBaseFragment<V extends TestFragmentBaseView, P extends TestBasePresenter<V>> extends BaseFragment<V, P>
        implements TestFragmentBaseView {

    private static final String TAG_ALERT = "alert";

    private TestFragmentComponent testFragmentComponent;

    @BindView(R.id.header)
    protected HeaderView titleHeaderView;
    @BindView(R.id.testVideo)
    @Nullable
    protected FrameVideoView videoView;

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
        titleHeaderView.setTitle(title);
    }

    @Override
    public void setMessage(@NonNull final String message) {
        titleHeaderView.setSubtitle(message);
    }

    @Override
    public void showAlertDialog(@NonNull final AlertDialogModel alertDialogModel, @Nullable final AlertDialogDecisionListener listener) {
        final AlertDialogFragment alertDialogFragment = AlertDialogFragment.create(alertDialogModel, listener);
        alertDialogFragment.show(getChildFragmentManager(), TAG_ALERT);
    }


    @Override
    public void playVideo(@NonNull final Uri uri, final boolean shouldLoop) {
        if (videoView != null) {

            videoView.setup(uri, ContextCompat.getColor(getActivity(), R.color.applicationWhite));
            videoView.setFrameVideoViewListener(new FrameVideoViewListener() {
                @Override
                public void mediaPlayerPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(shouldLoop);
                    mediaPlayer.start();
                }

                @Override
                public void mediaPlayerPrepareFailed(MediaPlayer mediaPlayer, String s) {

                }
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
    public void setDisclaimerViewVisible(final boolean visible) {
        runOnTestTestActivityView(view -> view.setDisclaimerViewVisible(visible));
    }

    @Override
    public void setDisclaimerMessage(@NonNull final String message) {
        runOnTestTestActivityView(view -> view.setDisclaimerMessage(message));
    }

    @Override
    public void setNextButtonVisible(final boolean visible) {
        runOnTestTestActivityView(view -> view.setNextButtonVisible(visible));
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
        if (videoView != null) {
            videoView.setFrameVideoViewListener(null);
        }

        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (videoView != null) {
            videoView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (videoView != null) {
            videoView.onPause();
        }

        super.onPause();
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
