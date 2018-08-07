package com.aptatek.pkuapp.view.test.incubation;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.model.AlertDialogModel;
import com.aptatek.pkuapp.view.test.base.TestFragmentBaseView;

public interface IncubationView extends TestFragmentBaseView {

    void showCountdownText(@NonNull String text);

    void showAlertDialog(@NonNull AlertDialogModel model);

}
