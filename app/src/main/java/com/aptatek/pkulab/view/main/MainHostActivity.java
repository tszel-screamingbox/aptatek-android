package com.aptatek.pkulab.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.main.home.HomeFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.HIDDEN;

public class MainHostActivity extends BaseActivity<MainHostActivityView, MainHostActivityPresenter> implements MainHostActivityView {


    @Inject
    MainHostActivityPresenter presenter;

    @BindView(R.id.panelLayout)
    SlidingUpPanelLayout mainSlidingUpPanelLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        switchToFragment(new HomeFragment());
        mainSlidingUpPanelLayout.setEnabled(false);
        mainSlidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(final View panel, final float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(final View panel, final SlidingUpPanelLayout.PanelState previousState, final SlidingUpPanelLayout.PanelState newState) {

            }
        });
    }

    public void enableSlidingPanel() {
        mainSlidingUpPanelLayout.setEnabled(true);
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
        if (mainSlidingUpPanelLayout.getPanelState() == EXPANDED) {
            mainSlidingUpPanelLayout.setPanelState(COLLAPSED);
            return;
        }

        super.onBackPressed();
    }
}
