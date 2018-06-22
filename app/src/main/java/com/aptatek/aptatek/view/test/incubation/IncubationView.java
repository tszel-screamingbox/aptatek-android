package com.aptatek.aptatek.view.test.incubation;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.model.AlertDialogModel;
import com.aptatek.aptatek.view.test.base.TestFragmentBaseView;

public interface IncubationView extends TestFragmentBaseView {

    void showCountdownText(@NonNull String text);

    void showAlertDialog(@NonNull AlertDialogModel model);

}
