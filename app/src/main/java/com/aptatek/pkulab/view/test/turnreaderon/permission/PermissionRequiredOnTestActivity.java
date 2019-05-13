package com.aptatek.pkulab.view.test.turnreaderon.permission;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

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
