package com.aptatek.pkuapp.view.test.base;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.FragmentComponent;
import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.view.base.BaseFragment;
import com.aptatek.pkuapp.view.test.TestActivityView;

import butterknife.BindView;

public abstract class TestBaseFragment<V extends TestFragmentBaseView, P extends TestBasePresenter<V>> extends BaseFragment<V, P>
    implements TestFragmentBaseView {

    private TestFragmentComponent testFragmentComponent;

    @BindView(R.id.testTitle)
    protected TextView tvTitle;
    @BindView(R.id.testMessage)
    protected TextView tvMessage;
    @Nullable
    @BindView(R.id.testAlertText)
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
    public void setAlertViewVisible(final boolean visible) {
        if (tvAlert != null) {
            tvAlert.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setAlertMessage(@NonNull final String message) {
        if (tvAlert != null) {
            tvAlert.setText(message);
        }
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
        if (getActivity() instanceof TestActivityView) {
            ((TestActivityView) getActivity()).setBottomBarVisible(visible);
        }
    }

    @Override
    public void setBatteryIndicatorVisible(final boolean visible) {

    }

    @Override
    public void setBatteryPercentageText(@NonNull final String percentageText) {

    }

    @Override
    public void setProgressVisible(final boolean visible) {

    }

    @Override
    public void setProgressPercentage(final int percentage) {

    }

    @Override
    protected void initObjects(final View view) {
        presenter.initUi();
    }

    @Override
    public void onStop() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.stopPlayback();
            videoView.setOnPreparedListener(null);
        }

        super.onStop();
    }
}
