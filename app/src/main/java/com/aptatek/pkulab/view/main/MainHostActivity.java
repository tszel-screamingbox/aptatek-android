package com.aptatek.pkulab.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

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

public class MainHostActivity extends BaseActivity<MainHostActivityView, MainHostActivityPresenter> implements MainHostActivityView {


    @Inject
    MainHostActivityPresenter presenter;

    @BindView(R.id.panelLayout)
    SlidingUpPanelLayout mainSlidingPanelLayout;

    private HomeFragment homeFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        homeFragment = new HomeFragment();
        switchToFragment(homeFragment);


        mainSlidingPanelLayout.setEnabled(false);
    }

    public void enableSlidingPanel() {
        mainSlidingPanelLayout.setEnabled(true);
    }

    public void showWeeklyChartPanel() {
        mainSlidingPanelLayout.setPanelState(EXPANDED);
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

        if (homeFragment.isResultShown()) {
            homeFragment.closeResultsPanel();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (homeFragment.handleDispatchTouchEvent(ev) || mainSlidingPanelLayout.getPanelState()
                == SlidingUpPanelLayout.PanelState.DRAGGING && ev.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }
}
