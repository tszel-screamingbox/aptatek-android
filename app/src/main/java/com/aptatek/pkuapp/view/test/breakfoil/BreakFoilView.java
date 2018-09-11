package com.aptatek.pkuapp.view.test.breakfoil;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.model.AlertDialogModel;
import com.aptatek.pkuapp.view.test.base.TestFragmentBaseView;

public interface BreakFoilView extends TestFragmentBaseView {

    void showAlert(@NonNull AlertDialogModel alertDialogModel);

}
