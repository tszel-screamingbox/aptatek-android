package com.aptatek.pkulab.domain.model;

import android.content.Context;

import com.aptatek.pkulab.R;

public class TestContinueDialogModel {

    public static AlertDialogModel continueTestDialogModelCreate(final Context context) {
        return AlertDialogModel.builder()
                .setTitle(context.getString(R.string.home_test_continue_dialog_title))
                .setMessage(context.getString(R.string.home_test_continue_dialog_message))
                .setPositiveButtonText(context.getString(R.string.home_test_continue_dialog_next))
                .setCancelable(false)
                .build();
    }

    public static AlertDialogModel unfinishedTestDialogModelCreate(final Context context) {
        return AlertDialogModel.builder()
                .setTitle(context.getString(R.string.home_test_unfinished_title))
                .setMessage(context.getString(R.string.home_test_unfinished_message))
                .setPositiveButtonText(context.getString(R.string.alertdialog_button_yes))
                .setNegativeButtonText(context.getString(R.string.alertdialog_button_no))
                .setCancelable(false)
                .build();
    }

    public static AlertDialogModel incorrectResultDialogModelCreate(final Context context) {
        return AlertDialogModel.builder()
                .setTitle(context.getString(R.string.home_test_continue_dialog_cannot_be_finished_title))
                .setMessage(context.getString(R.string.home_test_continue_dialog_cannot_be_finished_message))
                .setPositiveButtonText(context.getString(R.string.home_test_continue_dialog_next))
                .setCancelable(false)
                .build();
    }
}
