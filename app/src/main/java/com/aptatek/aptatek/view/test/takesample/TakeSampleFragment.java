package com.aptatek.aptatek.view.test.takesample;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.FragmentComponent;
import com.aptatek.aptatek.view.test.base.TestBaseFragment;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class TakeSampleFragment extends TestBaseFragment<TakeSampleView, TakeSamplePresenter>
        implements TakeSampleView {

    @Inject
    TakeSamplePresenter takeSamplePresenter;

    @BindView(R.id.takeSampleVideo)
    VideoView videoView;
    @BindView(R.id.takeSampleAgeToggle)
    TextView ageToggle;
    @BindView(R.id.takeSamplePlay)
    View playIcon;
    @BindView(R.id.takeSampleVideoThumbnail)
    ImageView videoThumbnail;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test_start;
    }

    @Override
    protected void initObjects(final View view) {
        super.initObjects(view);
        takeSamplePresenter.initUi();
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public void showAgeSwitcherText(@NonNull final String text) {
        final SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length() - 1, 0);
        ageToggle.setText(spannableString);
    }

    @Override
    public void loadVideo(@NonNull final Uri video) {
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }

        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(mp ->
            playIcon.setVisibility(View.VISIBLE)
        );
        videoView.setOnCompletionListener(mp -> {
            videoThumbnail.setVisibility(View.VISIBLE);
            playIcon.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void showVideoThumbnail(@NonNull final Bitmap thumbnail) {
        final ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            final ConstraintLayout.LayoutParams constraintLayoutParams = (ConstraintLayout.LayoutParams) layoutParams;
            constraintLayoutParams.dimensionRatio = String.format(Locale.getDefault(), "W,%d:%d", thumbnail.getWidth(), thumbnail.getHeight());
        }

        videoThumbnail.setImageBitmap(thumbnail);
        videoThumbnail.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNavigateForwardPressed() {
        presenter.startIncubation();
    }

    @NonNull
    @Override
    public TakeSamplePresenter createPresenter() {
        return takeSamplePresenter;
    }

    @OnClick(R.id.takeSampleAgeToggle)
    void onAgeToggled() {
        takeSamplePresenter.onChangeAge();
    }

    @OnClick(R.id.takeSamplePlay)
    void onClickPlay() {
        videoView.start();
        playIcon.setVisibility(View.GONE);
        videoThumbnail.setVisibility(View.GONE);
    }
}
