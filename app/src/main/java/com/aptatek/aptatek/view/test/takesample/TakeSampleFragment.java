package com.aptatek.aptatek.view.test.takesample;

import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.FragmentComponent;
import com.aptatek.aptatek.view.test.base.TestBaseFragment;

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
    public void loadVideo(@NonNull final String video) {
        videoView.setVideoPath(video);
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
}
