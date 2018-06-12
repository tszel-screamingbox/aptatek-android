package com.aptatek.aptatek.view.test.incubation;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.test.TestFragmentComponent;
import com.aptatek.aptatek.view.test.TestScreens;
import com.aptatek.aptatek.view.test.base.TestBaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import timber.log.Timber;

public class IncubationFragment extends TestBaseFragment<IncubationView, IncubationPresenter>
    implements IncubationView {

    @Inject
    IncubationPresenter presenter;

    @BindView(R.id.incubationCounter)
    TextView tvCountDown;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_incubation;
    }

    @Override
    protected void initObjects(@NonNull final View view) {
        super.initObjects(view);

        presenter.initUi();
    }

    @Override
    protected void injectTestFragment(@NonNull final TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @NonNull
    @Override
    public IncubationPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void showCountdownText(@NonNull final String text) {
        tvCountDown.setText(text);
    }

    @Override
    public boolean onNavigateBackPressed() {
        showScreen(TestScreens.CANCEL);

        return true;
    }

    @Override
    public boolean onNavigateForwardPressed() {
        Timber.d("onNavigateForwardPressed");

        return false;
    }

}
