package com.aptatek.pkulab.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.main.home.HomeFragment;
import com.aptatek.pkulab.view.main.weekly.WeeklyResultFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;

public class MainHostActivity extends BaseActivity<MainHostActivityView, MainHostActivityPresenter> implements MainHostActivityView {


    @Inject
    MainHostActivityPresenter presenter;

    @BindView(R.id.panelLayout)
    SlidingUpPanelLayout mainSlidingPanelLayout;

    private WeeklyResultFragment weeklyResultFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        switchToFragment(new HomeFragment());

        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.weeklyPanel);
        weeklyResultFragment = (WeeklyResultFragment) fragment;
        weeklyResultFragment.hideCompleteHeader();

        mainSlidingPanelLayout.setEnabled(false);
        mainSlidingPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(final View panel, final float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(final View panel, final SlidingUpPanelLayout.PanelState previousState, final SlidingUpPanelLayout.PanelState newState) {
                if (newState == COLLAPSED) {
                    weeklyResultFragment.showHeader();
                }
                if (newState == EXPANDED) {
                    weeklyResultFragment.hideHeader();
                }
            }
        });
    }

    public void enableSlidingPanel() {
        mainSlidingPanelLayout.setEnabled(true);
        weeklyResultFragment.showHeader();
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @NonNull
    @Override
    public MainHostActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.mainFrame;
    }

    @Override
    public void onBackPressed() {
        if (mainSlidingPanelLayout.getPanelState() == EXPANDED) {
            mainSlidingPanelLayout.setPanelState(COLLAPSED);
            return;
        }

        super.onBackPressed();
    }
}
