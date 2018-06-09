package com.aptatek.aptatek.view.test.takesample;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.VideoThumbnailInteractor;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

public class TakeSamplePresenter extends MvpBasePresenter<TakeSampleView> {

    private final ResourceInteractor resourceInteractor;
    private final VideoThumbnailInteractor videoThumbnailInteractor;
    private final IncubationInteractor incubationInteractor;

    private boolean showAdult;

    @Inject
    public TakeSamplePresenter(final ResourceInteractor resourceInteractor,
                               final VideoThumbnailInteractor videoThumbnailInteractor,
                               final IncubationInteractor incubationInteractor) {
        this.resourceInteractor = resourceInteractor;
        this.videoThumbnailInteractor = videoThumbnailInteractor;
        this.incubationInteractor = incubationInteractor;
    }

    @Override
    public void attachView(final @NonNull TakeSampleView view) {
        super.attachView(view);

        showAdult = true;
    }

    public void initUi() {
        ifViewAttached(view -> {
            view.setTitle(resourceInteractor.getStringResource(R.string.test_takesample_title));
            view.setMessage(resourceInteractor.getStringResource(R.string.test_takesample_description));
            view.setNavigationButtonVisible(true);
            view.setNavigationButtonText(resourceInteractor.getStringResource(R.string.test_takesample_button_start));
            view.setCancelBigVisible(false);
            view.setCircleCancelVisible(true);
        });
        onChangeAge();
    }

    public void onChangeAge() {
        showAdult = !showAdult;
        ifViewAttached(view -> {
            final Uri uriForRawFile = resourceInteractor.getUriForRawFile(showAdult ? R.raw.big_buck_bunny : R.raw.big_buck_bunny);
            view.showVideoThumbnail(videoThumbnailInteractor.createThumbnailForRawVideo(uriForRawFile));
            view.loadVideo(uriForRawFile);
            view.showAgeSwitcherText(resourceInteractor.getStringResource(R.string.test_takesample_age_switch,
                    resourceInteractor.getStringResource(showAdult ? R.string.test_takesample_age_child : R.string.test_takesample_age_adult)));
        });
    }

    public void startIncubation() {
        incubationInteractor.startIncubation().subscribe();
    }
}
