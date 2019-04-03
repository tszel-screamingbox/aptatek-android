package com.aptatek.pkulab.view.parentalgate.welcome;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.injection.module.parentalgate.ParentalGateModule;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.parentalgate.ParentalGateView;
import com.aptatek.pkulab.view.parentalgate.verification.ParentalGateVerificationFragment;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.*;

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

    @BindView(R.id.keypad)
    View keypad;

    @BindView(R.id.buttonAction)
    TextView btnKeypadAction;

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
        presenter.initUi();
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.plus(new ParentalGateModule()).inject(this);
    }

    @NonNull
    @Override
    public ParentalGateWelcomePresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onDestroyView() {
        presenter.onCleared();

        super.onDestroyView();
    }

    @Override
    public void setShowButton(final boolean visible) {
        btnControl.setVisibility(visible ? VISIBLE : GONE);
    }

    @Override
    public void setButtonText(@NonNull final String text) {
        btnControl.setText(text);
    }

    @Override
    public void showDatePicker() {
        final Calendar now = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                R.style.Parental_Dialog_DatePicker,
            (view, year, month, dayOfMonth) -> {
                final Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (presenter != null) {
                    presenter.onBirthDateSet(calendar.getTimeInMillis());
                }
            },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void setShowBirthDateField(final boolean visible) {
        etBirthDate.setVisibility(visible ? VISIBLE : GONE);
    }

    @Override
    public void setBirthDateText(@NonNull final String text) {
        etBirthDate.setText(text);
    }

    @Override
    public void setShowAgeField(final boolean visible) {
        etAge.setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    @Override
    public void setAgeText(@NonNull final String text) {
        etAge.setText(text);
    }

    @Override
    public void setShowKeypad(final boolean visible) {
        keypad.setVisibility(visible ? VISIBLE : GONE);
    }

    @Override
    public void setKeypadActionText(@NonNull final String text) {
        btnKeypadAction.setText(text);
    }

    @Override
    public void showResult(@NonNull final AgeVerificationResult result) {
        if (getActivity() instanceof ParentalGateView) {
            ((ParentalGateView) getActivity()).showScreen(ParentalGateVerificationFragment.createWithArguments(result));
        }
    }

    @OnClick(R.id.parentalButton)
    public void onControlButtonClicked() {
        presenter.onButtonPress();
    }

    @SuppressWarnings("cyclomaticComplexity")
    @OnClick({R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.buttonDelete})
    public void onKeypadClicked(final View receiver) {
        final String currentAge = etAge.getText().toString();
        final String newAge;

        switch (receiver.getId()) {
            case R.id.button0: {
                newAge = currentAge + "0";
                break;
            }
            case R.id.button1: {
                newAge = currentAge + "1";
                break;
            }
            case R.id.button2: {
                newAge = currentAge + "2";
                break;
            }
            case R.id.button3: {
                newAge = currentAge + "3";
                break;
            }
            case R.id.button4: {
                newAge = currentAge + "4";
                break;
            }
            case R.id.button5: {
                newAge = currentAge + "5";
                break;
            }
            case R.id.button6: {
                newAge = currentAge + "6";
                break;
            }
            case R.id.button7: {
                newAge = currentAge + "7";
                break;
            }
            case R.id.button8: {
                newAge = currentAge + "8";
                break;
            }
            case R.id.button9: {
                newAge = currentAge + "9";
                break;
            }
            case R.id.buttonDelete: {
                newAge = currentAge.length() > 0
                        ? currentAge.substring(0, currentAge.length() - 1)
                        : currentAge;
                break;
            }
            default: {
                newAge = currentAge;
                break;
            }
        }

        if (newAge.length() <= 2) {
            etAge.setText(newAge);
        }
    }

    @OnClick(R.id.buttonAction)
    public void onNextClicked() {
        presenter.verifyAge(etAge.getText().toString());
    }

    @OnClick(R.id.parentalBirthDate)
    public void onBirthDateClicked() {
        showDatePicker();
    }
}
