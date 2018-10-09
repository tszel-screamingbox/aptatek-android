package com.aptatek.pkulab.view.parentalgate.verification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.parentalgate.ParentalGateView;
import com.aptatek.pkulab.view.parentalgate.welcome.AgeVerificationResult;
import com.aptatek.pkulab.view.splash.SplashActivity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

public class ParentalGateVerificationFragment extends BaseFragment<ParentalGateVerificationView, ParentalGateVerificationPresenter>
        implements ParentalGateVerificationView {

    private static final String KEY_RESULT = "com.aptatek.parentalgate.verification.result";

    public static ParentalGateVerificationFragment createWithArguments(@NonNull final AgeVerificationResult ageVerificationResult) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_RESULT, ageVerificationResult);
        final ParentalGateVerificationFragment fragment = new ParentalGateVerificationFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

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

    private Disposable disposable;

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
        final AgeVerificationResult result;

        if (getArguments() != null) {
            result = getArguments().getParcelable(KEY_RESULT);
        } else {
            result = null;
        }

        if (result != null) {
            presenter.initUi(result);
        }
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
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

    @Override
    public void finishAfterDelay() {
        disposable = Completable.timer(2, TimeUnit.SECONDS).subscribe(() -> {
            if (getActivity() != null) {

                getActivity().startActivity(new Intent(getActivity(), SplashActivity.class));
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.onDestroyView();
    }

    @NonNull
    @Override
    public ParentalGateVerificationPresenter createPresenter() {
        return presenter;
    }

    @OnClick(R.id.parentalVerificationButton)
    public void onTryAgainClicked() {
        if (getActivity() instanceof ParentalGateView) {
            ((ParentalGateView) getActivity()).navigateBack();
        }
    }
}
