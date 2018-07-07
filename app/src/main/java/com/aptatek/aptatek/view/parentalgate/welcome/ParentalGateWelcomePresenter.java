package com.aptatek.aptatek.view.parentalgate.welcome;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.parentalgate.ParentalGateInteractor;
import com.aptatek.aptatek.domain.model.AgeCheckModel;
import com.aptatek.aptatek.domain.model.AgeCheckResult;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class ParentalGateWelcomePresenter extends MvpBasePresenter<ParentalGateWelcomeView> {

    private final ResourceInteractor resourceInteractor;
    private final ParentalGateInteractor parentalGateInteractor;

    private AgeCheckModel ageCheckModel;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ParentalGateWelcomePresenter(final ResourceInteractor resourceInteractor, final ParentalGateInteractor parentalGateInteractor) {
        this.resourceInteractor = resourceInteractor;
        this.parentalGateInteractor = parentalGateInteractor;
    }

    public void initUi() {
        ageCheckModel = AgeCheckModel.builder().build();

        ifViewAttached(attachedView -> {
            attachedView.setShowAgeField(false);
            attachedView.setShowBirthDateField(false);
            attachedView.setShowKeypad(false);
            attachedView.setShowButton(true);
            attachedView.setBirthDateText("");
            attachedView.setAgeText("");
            attachedView.setButtonText(resourceInteractor.getStringResource(R.string.parental_welcome_enter_birthday));
            attachedView.setKeypadActionText(resourceInteractor.getStringResource(R.string.parental_welcome_keypad_next));
        });
    }

    public void onButtonPress() {
        ifViewAttached(attachedView -> {
            if (ageCheckModel.getBirthDate() == 0L) {
                attachedView.showDatePicker();
            } else {
                attachedView.setShowButton(false);
                attachedView.setShowAgeField(true);
                attachedView.setShowKeypad(true);
            }
        });

    }

    public void onBirthDateSet(final long birthDate) {
        disposables.add(parentalGateInteractor.formatBirthDate(birthDate)
                .subscribe(formatted -> {
                    ageCheckModel = ageCheckModel.toBuilder()
                            .setBirthDate(birthDate)
                            .setBirthDateFormatted(formatted)
                            .build();

                    ifViewAttached(attachedView -> {
                        attachedView.setShowBirthDateField(true);
                        attachedView.setBirthDateText(ageCheckModel.getBirthDateFormatted());
                        attachedView.setAgeText("");
                        attachedView.setButtonText(resourceInteractor.getStringResource(R.string.parental_welcome_how_old_are_you));
                    });
                }));
    }

    public void verifyAge(final String ageText) {
        try {
            final int age = Integer.parseInt(ageText);

            ageCheckModel = ageCheckModel.toBuilder()
                    .setAge(age)
                    .build();

            disposables.add(parentalGateInteractor.verify(ageCheckModel)
                    .onErrorReturn(throwable -> AgeCheckResult.NotOldEnough)
                    .subscribe(result ->
                            ifViewAttached(attached -> attached.showResult(AgeVerificationResult.builder()
                                    .setIconRes(result == AgeCheckResult.ValidAge
                                            ? R.drawable.ic_age_verified
                                            : R.drawable.ic_age_not_verified)
                                    .setTitle(resourceInteractor.getStringResource(result == AgeCheckResult.ValidAge
                                            ? R.string.parental_verification_success_title
                                            : result == AgeCheckResult.NotOldEnough
                                            ? R.string.parental_verification_failure_title
                                            : R.string.parental_verification_failure_title_first))
                                    .setMessage(resourceInteractor.getStringResource(result == AgeCheckResult.ValidAge
                                            ? R.string.parental_verification_success_message
                                            : result == AgeCheckResult.NotOldEnough
                                            ? R.string.parental_verification_failure_message
                                            : R.string.parental_verification_failure_message_first))
                                    .setShowButton(result != AgeCheckResult.ValidAge)
                                    .build())
                            )
                    )
            );
        } catch (NumberFormatException e) {
            Timber.e(e);
        }
    }

    public void onCleared() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

}
