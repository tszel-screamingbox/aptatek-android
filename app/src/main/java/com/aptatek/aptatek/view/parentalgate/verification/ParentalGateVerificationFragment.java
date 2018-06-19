package com.aptatek.aptatek.view.parentalgate.verification;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.FragmentComponent;
import com.aptatek.aptatek.view.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ParentalGateVerificationFragment extends BaseFragment<ParentalGateVerificationView, ParentalGateVerificationPresenter>
        implements ParentalGateVerificationView {

    @Inject
    ParentalGateVerificationPresenter presenter;

    @BindView(R.id.parentalVerificationImage)
    ImageView ivImage;

    @BindView(R.id.parentalVerificationTitle)
    TextView tvTitle;

    @BindView(R.id.parentalVerificationMessage)
    TextView tvMessage;

    @BindView(R.id.parentalVerificationButton)
    View btnTryAgain;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_parental_verification;
    }

    @Override
    protected void initObjects(final @NonNull View view) {

    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {

    }

    @Override
    public void showImage(@DrawableRes final int imageRes) {
        ivImage.setImageResource(imageRes);
    }

    @Override
    public void showTitle(@NonNull final String title) {
        tvTitle.setText(title);
    }

    @Override
    public void showMessage(@NonNull final String message) {
        tvMessage.setText(message);
    }

    @Override
    public void showButton(final boolean visible) {
        btnTryAgain.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @NonNull
    @Override
    public ParentalGateVerificationPresenter createPresenter() {
        return presenter;
    }

    @OnClick(R.id.parentalVerificationButton)
    public void onTryAgainClicked() {
        // TODO navigate back
    }
}
