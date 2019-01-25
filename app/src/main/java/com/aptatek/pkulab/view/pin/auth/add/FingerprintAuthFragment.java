package com.aptatek.pkulab.view.pin.auth.add;

import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.widget.HeaderView;

import butterknife.BindView;

public class FingerprintAuthFragment extends AuthPinFragment {

    @BindView(R.id.header)
    HeaderView headerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fingerprint;
    }

    @Override
    protected void initObjects(final View view) {
        super.initObjects(view);
        headerView.setSubtitle(getString(R.string.auth_pin_hint_fingerprint));
    }
}
