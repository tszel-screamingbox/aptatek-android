package com.aptatek.pkuapp.view.test.breakfoil;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.model.AlertDialogModel;
import com.aptatek.pkuapp.view.test.base.TestBasePresenter;

import javax.inject.Inject;

public class BreakFoilPresenter extends TestBasePresenter<BreakFoilView> {

    @Inject
    public BreakFoilPresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_breakfoil_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_breakfoil_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.break_foil), true);
            attachedView.showAlertDialog(
                    AlertDialogModel.builder()
                    .setTitle(resourceInteractor.getStringResource(R.string.test_breakfoil_alert_title))
                    .setMessage(resourceInteractor.getStringResource(R.string.test_breakfoil_alert_message))
                    .setCancelable(false)
                    .setNeutralButtonText(resourceInteractor.getStringResource(android.R.string.ok))
                    .build(), null);
        });
    }

}
