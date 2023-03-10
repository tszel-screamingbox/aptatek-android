package com.aptatek.pkulab.view.test.base;

import android.net.Uri;
import android.text.Spanned;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisionListener;
import com.aptatek.pkulab.view.test.TestActivityCommonView;
import com.aptatek.pkulab.view.test.TestScreens;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface TestFragmentBaseView extends MvpView, TestActivityCommonView {

    void setTitle(@NonNull String title);

    void setMessage(@NonNull String message);

    void setMessageHtml(@NonNull Spanned spannable);

    void showAlertDialog(@NonNull AlertDialogModel alertDialogModel, @Nullable AlertDialogDecisionListener listener);

    void playVideo(@NonNull Uri uri, boolean shouldLoop);

    boolean onNextPressed();

    TestScreens getScreen();

}
