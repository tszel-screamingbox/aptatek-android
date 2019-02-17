package com.aptatek.pkulab.view.test.turnreaderon.permission;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.aptatek.pkulab.R;

public class PermissionRequiredOnTestActivity extends AppCompatActivity implements PermissionRequiredOnTestView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_content_frame);

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new PermissionRequiredOnTestFragment());
        fragmentTransaction.commitNow();
    }

    @Override
    public void onConditionsMet() {
        finish();
    }
}
