package com.aptatek.aptatek.view.parentalgate.welcome;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.FragmentComponent;
import com.aptatek.aptatek.view.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ParentalGateWelcomeFragment extends BaseFragment<ParentalGateWelcomeView, ParentalGateWelcomePresenter>
        implements ParentalGateWelcomeView {

    @Inject
    ParentalGateWelcomePresenter presenter;

    @BindView(R.id.parentalBirthDate)
    EditText etBirthDate;

    @BindView(R.id.parentalAge)
    EditText etAge;

    @BindView(R.id.parentalButton)
    Button btnControl;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_parental_welcome;
    }

    @Override
    protected void initObjects(final View view) {

    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {

    }

    @NonNull
    @Override
    public ParentalGateWelcomePresenter createPresenter() {
        return presenter;
    }

    @Override
    public void showButton(final boolean visible) {
        btnControl.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setButtonText(@NonNull final String text) {
        btnControl.setText(text);
    }

    @Override
    public void showBirthDateField(final boolean visible) {
        etBirthDate.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setBirthDateText(@NonNull final String text) {
        etBirthDate.setText(text);
    }

    @Override
    public void showAgeField(final boolean visible) {
        etAge.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @OnClick(R.id.parentalButton)
    public void onControlButtonClicked() {
        // TODO check status and act accordingly
    }

}
