package com.aptatek.pkulab.view.error;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.view.main.MainHostActivity;
import com.aptatek.pkulab.widget.HeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ErrorActivity extends Activity {

    @BindView(R.id.error_header)
    HeaderView headerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_error);

        ButterKnife.bind(this);

        final ErrorModel model = getModel();
        if (model != null) {
            headerView.setTitle(model.getTitle());
            headerView.setSubtitle(model.getMessage());
        }
    }

    public static Intent starter(@NonNull Context context, @NonNull final ErrorModel errorModel) {
        final Intent intent = new Intent(context, ErrorActivity.class);
        final Bundle extras = new Bundle();
        extras.putParcelable("model", errorModel);
        intent.putExtras(extras);
        return intent;
    }

    @Nullable
    private ErrorModel getModel() {
        return getIntent().getParcelableExtra("model");
    }


    @OnClick(R.id.error_done)
    public void onClickDone() {
        finish();
        final Intent intent = new Intent(this, MainHostActivity.class);
        launchActivity(intent, true);
    }

    private void launchActivity(final Intent intent, final boolean clearHistory) {
        if (clearHistory) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

}
