package com.aptatek.aptatek.view.test.incubation;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.FragmentComponent;
import com.aptatek.aptatek.injection.component.test.TestFragmentComponent;
import com.aptatek.aptatek.view.test.base.TestBaseFragment;

import javax.inject.Inject;

import butterknife.BindView;

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
    protected void injectFragment(@NonNull final FragmentComponent fragmentComponent) {
    }

    @Override
    protected void injectIncubationFragment(@NonNull final TestFragmentComponent fragmentComponent) {
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
    public void onNavigateBackPressed() {
        Toast.makeText(getActivity(), "Tuti megszakitod az incubationt?", Toast.LENGTH_SHORT).show();
    }
}
