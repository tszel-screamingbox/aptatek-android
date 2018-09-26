package com.aptatek.pkuapp.view.connect.turnon;

import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.FragmentComponent;
import com.aptatek.pkuapp.view.base.BaseFragment;

import javax.inject.Inject;

import butterknife.OnClick;

public class TurnOnFragment extends BaseFragment<TurnOnView, TurnOnPresenter> implements TurnOnView {

    @Inject
    TurnOnPresenter presenter;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_turn_on;
    }

    @Override
    protected void initObjects(View view) {

    }

    @Override
    protected void injectFragment(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @NonNull
    @Override
    public TurnOnPresenter createPresenter() {
        return presenter;
    }

    @OnClick(R.id.turnOnNext)
    public void onNextClick() {
        // TODO get permissions
    }
}
