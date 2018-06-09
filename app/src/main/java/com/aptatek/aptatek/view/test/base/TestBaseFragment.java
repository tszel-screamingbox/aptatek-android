package com.aptatek.aptatek.view.test.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aptatek.aptatek.AptatekApplication;
import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.test.DaggerTestFragmentComponent;
import com.aptatek.aptatek.injection.component.test.TestFragmentComponent;
import com.aptatek.aptatek.injection.module.FragmentModule;
import com.aptatek.aptatek.injection.module.test.IncubationModule;
import com.aptatek.aptatek.view.base.BaseFragment;
import com.aptatek.aptatek.view.test.TestActivityView;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class TestBaseFragment<V extends TestFragmentBaseView, P extends MvpPresenter<V>> extends BaseFragment<V, P>
    implements TestFragmentBaseView {

    @BindView(R.id.testBaseTitle)
    TextView tvTitle;
    @BindView(R.id.testBaseMessage)
    TextView tvDescription;

    private TestFragmentComponent testFragmentComponent;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        injectIncubationFragment(getTestFragmentComponent());
        super.onCreate(savedInstanceState);
    }

    protected TestFragmentComponent getTestFragmentComponent() {
        if (testFragmentComponent == null) {
            testFragmentComponent = DaggerTestFragmentComponent.builder()
                    .fragmentModule(new FragmentModule(this))
                    .incubationModule(new IncubationModule())
                    .applicationComponent(AptatekApplication.get(getContext()).getApplicationComponent())
                    .build();
        }

        return testFragmentComponent;
    }

    protected abstract void injectIncubationFragment(TestFragmentComponent fragmentComponent);

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_base;
    }

    @Override
    protected void initObjects(final View view) {
        final FrameLayout flContent = view.findViewById(R.id.testBaseContent);
        flContent.removeAllViews();
        LayoutInflater.from(view.getContext()).inflate(getContentLayoutId(), flContent);
        ButterKnife.bind(this, view);
    }

    @Override
    public void setTitle(@NonNull final String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setMessage(@NonNull final String message) {
        tvDescription.setText(message);
    }

    @Override
    public void setCircleCancelVisible(final boolean visible) {
        runOnTestActivityView(view -> view.setCircleCancelVisible(visible));
    }

    @Override
    public void setCancelBigVisible(final boolean visible) {
        runOnTestActivityView(view -> view.setCancelBigVisible(visible));
    }

    @Override
    public void setNavigationButtonVisible(final boolean visible) {
        runOnTestActivityView(view -> view.setNavigationButtonVisible(visible));
    }

    @Override
    public void setNavigationButtonText(@NonNull final String buttonText) {
        runOnTestActivityView(view -> view.setNavigationButtonText(buttonText));
    }

    @Override
    public void onNavigateBackPressed() {

    }

    @Override
    public void onNavigateForwardPressed() {

    }

    @Override
    public void navigateBack() {
        runOnTestActivityView(TestActivityView::navigateBack);
    }

    @Override
    public void navigateForward() {
        runOnTestActivityView(TestActivityView::navigateForward);
    }

    private void runOnTestActivityView(final TestActivityViewAction action) {
        if (getActivity() instanceof TestActivityView) {
            action.run((TestActivityView) getActivity());
        }
    }

    @LayoutRes
    protected abstract int getContentLayoutId();

    private interface TestActivityViewAction {

        void run(TestActivityView view);

    }
}
