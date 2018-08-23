package com.aptatek.pkuapp.view.test.takesample;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.VideoThumbnailInteractor;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.pkuapp.view.test.base.TestBasePresenter;

import javax.inject.Inject;

public class TakeSamplePresenter extends TestBasePresenter<TakeSampleView> {

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

        showAdult = false;
    }

    @Override
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
            view.showAgeSwitcherText(
                resourceInteractor.getStringResource(showAdult ? R.string.test_takesample_ageswitch_adult : R.string.test_takesample_ageswitch_infant));
        });
    }

    public void startIncubation() {
        incubationInteractor.startIncubation().subscribe();
    }
}
