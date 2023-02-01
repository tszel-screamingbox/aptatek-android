package com.aptatek.pkulab.view.parentalgate.welcome;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.parentalgate.ParentalGateInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;
import com.aptatek.pkulab.domain.manager.analytic.events.onboarding.OnboardingParentalDone;
import com.aptatek.pkulab.domain.manager.analytic.events.onboarding.OnboardingParentalFailed;
import com.aptatek.pkulab.domain.model.AgeCheckModel;
import com.aptatek.pkulab.domain.model.AgeCheckResult;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class ParentalGateWelcomePresenter extends MvpBasePresenter<ParentalGateWelcomeView> {

    private final ResourceInteractor resourceInteractor;
    private final ParentalGateInteractor parentalGateInteractor;
    private final IAnalyticsManager analyticsManager;

    private AgeCheckModel ageCheckModel;
    private CompositeDisposable disposables = new CompositeDisposable();

    private long attachTimeMs = 0L;

    @Override
    public void attachView(final @NonNull ParentalGateWelcomeView view) {
        super.attachView(view);

        if (attachTimeMs == 0L) {
            attachTimeMs = System.currentTimeMillis();
        }
    }

    @Inject
    public ParentalGateWelcomePresenter(final ResourceInteractor resourceInteractor,
                                        final ParentalGateInteractor parentalGateInteractor,
                                        final IAnalyticsManager analyticsManager) {
        this.resourceInteractor = resourceInteractor;
        this.parentalGateInteractor = parentalGateInteractor;
        this.analyticsManager = analyticsManager;
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
        if (birthDate > System.currentTimeMillis()) {
            ifViewAttached(av -> av.showToastWithMessage(R.string.parental_birthday_invalid));
        } else {
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
    }

    public void verifyAge(final String ageText) {
        try {
            final int age = Integer.parseInt(ageText);

            ageCheckModel = ageCheckModel.toBuilder()
                    .setAge(age)
                    .build();

            disposables.add(parentalGateInteractor.verify(ageCheckModel)
                    .onErrorReturn(throwable -> AgeCheckResult.NOT_OLD_ENOUGH)
                    .subscribe(result -> {

                                final AnalyticsEvent event = result == AgeCheckResult.VALID_AGE
                                        ? new OnboardingParentalDone(System.currentTimeMillis() - attachTimeMs)
                                        : new OnboardingParentalFailed();

                                analyticsManager.logEvent(event);

                                ifViewAttached(attached -> attached.showResult(AgeVerificationResult.builder()
                                        .setIconRes(result == AgeCheckResult.VALID_AGE
                                                ? R.drawable.ic_age_verified
                                                : R.drawable.ic_age_not_verified)
                                        .setTitle(resourceInteractor.getStringResource(result == AgeCheckResult.VALID_AGE
                                                ? R.string.parental_verification_success_title
                                                : result == AgeCheckResult.NOT_OLD_ENOUGH
                                                ? R.string.parental_verification_failure_not_old_enough_title
                                                : R.string.parental_verification_failure_age_not_match_title))
                                        .setMessage(resourceInteractor.getStringResource(result == AgeCheckResult.VALID_AGE
                                                ? R.string.parental_verification_success_message
                                                : result == AgeCheckResult.NOT_OLD_ENOUGH
                                                ? R.string.parental_verification_failure_not_old_enough_message
                                                : R.string.parental_verification_failure_age_not_match_message))
                                        .setShowButton(result != AgeCheckResult.VALID_AGE)
                                        .build())
                                );

                            }
                    )
            );
        } catch (final NumberFormatException e) {
            Timber.e(e);
        }
    }

    public void onCleared() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

}
