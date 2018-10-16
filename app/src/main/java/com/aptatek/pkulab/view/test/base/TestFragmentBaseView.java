package com.aptatek.pkulab.view.test.base;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisionListener;
import com.aptatek.pkulab.view.test.TestActivityCommonView;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface TestFragmentBaseView extends MvpView, TestActivityCommonView {

    void setTitle(@NonNull String title);

    void setMessage(@NonNull String message);

    void setDisclaimerViewVisible(boolean visible);

    void setDisclaimerMessage(@NonNull String message);

    void showAlertDialog(@NonNull AlertDialogModel alertDialogModel, @Nullable AlertDialogDecisionListener listener);

    void playVideo(@NonNull Uri uri, boolean shouldLoop);

    boolean onNextPressed();

}
