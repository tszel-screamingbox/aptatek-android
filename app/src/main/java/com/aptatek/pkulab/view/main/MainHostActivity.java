package com.aptatek.pkulab.view.main;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.main.home.HomeFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainHostActivity extends BaseActivity<MainHostActivityView, MainHostActivityPresenter> implements MainHostActivityView {


    @Inject
    MainHostActivityPresenter presenter;

    @BindView(R.id.panelLayout)
    SlidingUpPanelLayout mainSlidingPanelLayout;

    private HomeFragment homeFragment;

    private long weeklyPanelShownMs = 0L;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        homeFragment = new HomeFragment();
        switchToFragment(homeFragment);

        mainSlidingPanelLayout.setEnabled(false);
        mainSlidingPanelLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(final View panel, final float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(final View panel, final PanelState previousState, final PanelState newState) {
                if (newState == COLLAPSED) {
                    presenter.logWeeklyChartClosed(Math.abs(System.currentTimeMillis() - weeklyPanelShownMs));
                }
            }
        });
    }

    public void enableSlidingPanel() {
        mainSlidingPanelLayout.setEnabled(true);
    }

    public void showWeeklyChartPanel() {
        mainSlidingPanelLayout.setPanelState(EXPANDED);
        weeklyPanelShownMs = System.currentTimeMillis();
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

            presenter.logWeeklyChartClosed(Math.abs(System.currentTimeMillis() - weeklyPanelShownMs));
            weeklyPanelShownMs = 0L;

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
                == PanelState.DRAGGING && ev.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homeFragment.onActivityResult(requestCode, resultCode, data);
    }
}
