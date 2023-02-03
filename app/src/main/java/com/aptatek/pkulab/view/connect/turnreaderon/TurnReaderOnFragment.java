package com.aptatek.pkulab.view.connect.turnreaderon;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.onboarding.OnboardingMultipleReaderFound;
import com.aptatek.pkulab.domain.manager.analytic.events.onboarding.OnboardingNoReaderAvailable;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.connect.onboarding.ConnectOnboardingReaderActivity;
import com.aptatek.pkulab.view.connect.onboarding.turnon.TurnReaderOnConnectFragment;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.scan.ScanDialogFragment;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisionListener;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.error.ErrorModel;
import com.aptatek.pkulab.view.test.TestActivity;
import com.aptatek.pkulab.widget.HeaderView;
import com.mklimek.frameviedoview.FrameVideoView;
import com.mklimek.frameviedoview.FrameVideoViewListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ix.Ix;
import timber.log.Timber;

public abstract class TurnReaderOnFragment<V extends TurnReaderOnView, P extends TurnReaderOnPresenter<V>> extends BaseFragment<V, P> implements TurnReaderOnView, ScanDialogFragment.ScanListener {

    private static final String TAG_SCAN = "pkulab.scan.devices";
    private static final String TAG_NOT_SUPPORTED = "pkulab.scan.devicenotsupported";
    private static final int REQ_PERMISSION = 737;
    private static final String PKULAB_TEST_ALERT = "pkulab.test.alert";

    @BindView(R.id.header)
    protected HeaderView headerView;
    @BindView(R.id.testVideo)
    protected FrameVideoView videoView;
    @BindView(R.id.turnReaderOnNoDeviceAvailable)
    protected Button noReaderAvailable;
    @BindView(R.id.self_check)
    protected TextView selfCheck;

    @BindView(R.id.container)
    protected ConstraintLayout container;

    @BindView(R.id.sync_container)
    protected ConstraintLayout syncContainer;

    @BindView(R.id.syncProgress)
    protected ProgressBar syncProgressBar;

    @BindView(R.id.syncText)
    protected TextView syncText;

    @Inject
    ResourceInteractor resourceInteractor;

    @Inject
    IAnalyticsManager analyticsManager;

    private Disposable restartVideoDisposable;

    private long screentime = 0L;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected List<View> sensitiveViewList() {
        return Collections.emptyList();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_turnreaderon;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null || screentime == 0L) {
            screentime = System.currentTimeMillis();
        }

        presenter.logScreenDisplayed();
    }

    @Override
    protected void initObjects(final View view) {
        headerView.setTitle(resourceInteractor.getStringResource(R.string.test_turnreaderon_title));
        headerView.setSubtitle(resourceInteractor.getStringResource(R.string.test_turnreaderon_message));
        playVideo(resourceInteractor.getUriForRawFile(R.raw.turn_reader_on), false);

        if (getActivity() instanceof TestActivity) {
            final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) noReaderAvailable.getLayoutParams();
            layoutParams.bottomMargin += resourceInteractor.getDimension(R.dimen.test_bottombar_height);
        }
    }

    protected void playVideo(@NonNull final Uri uri, final boolean shouldLoop) {
        if (restartVideoDisposable != null && !restartVideoDisposable.isDisposed()) {
            restartVideoDisposable.dispose();
            restartVideoDisposable = null;
        }

        videoView.setup(uri, ContextCompat.getColor(requireContext(), R.color.applicationWhite));
        videoView.setFrameVideoViewListener(new FrameVideoViewListener() {
            @Override
            public void mediaPlayerPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(shouldLoop);
                mediaPlayer.start();

                if (!shouldLoop) {
                    mediaPlayer.setOnCompletionListener(mp -> {
                        restartVideoDisposable = Single.timer(5L, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(ignored -> {
                                    try {
                                        mediaPlayer.start();
                                    } catch (Throwable t) {
                                        Timber.e(t, "Failed to restart video loop!");
                                    }
                                });
                    });
                }
            }

            @Override
            public void mediaPlayerPrepareFailed(MediaPlayer mediaPlayer, String s) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        videoView.setFrameVideoViewListener(null);
    }

    @Override
    public void onStop() {
        if (restartVideoDisposable != null && !restartVideoDisposable.isDisposed()) {
            restartVideoDisposable.dispose();
            restartVideoDisposable = null;
        }


        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        videoView.onResume();
    }

    @Override
    public void onPause() {
        videoView.onPause();

        super.onPause();
    }

    @Override
    public void displayReaderSelector(@NonNull final List<ReaderDevice> readerDevices) {
        if (this instanceof TurnReaderOnConnectFragment) {
            analyticsManager.logEvent(new OnboardingMultipleReaderFound());
        }

        ScanDialogFragment scanDialogFragment = findScanDialogFragment();
        if (scanDialogFragment == null) {
            scanDialogFragment = ScanDialogFragment.create(
                    this,
                    isSkipable()
            );
            scanDialogFragment.show(getChildFragmentManager(), TAG_SCAN);
        }

        scanDialogFragment.displayScanResults(readerDevices);
    }

    @Nullable
    private ScanDialogFragment findScanDialogFragment() {
        return ((ScanDialogFragment) getChildFragmentManager().findFragmentByTag(TAG_SCAN));
    }

    @Override
    public void displaySelfCheckAnimation() {
        playVideo(resourceInteractor.getUriForRawFile(R.raw.self_checking), true);
        noReaderAvailable.setVisibility(View.GONE);

        selfCheck.setVisibility(View.VISIBLE);
        headerView.setTitle(resourceInteractor.getStringResource(R.string.test_turnreaderon_selfcheck_title));
        headerView.setSubtitle(resourceInteractor.getStringResource(R.string.test_turnreaderon_selfcheck_message));
    }

    @Override
    public void displayNoReaderAvailable() {
        noReaderAvailable.setVisibility(View.VISIBLE);
        headerView.setSubtitle(getString(R.string.connect_turnon_no_reader_available_hint));
    }

    @Override
    public void showDeviceNotSupportedDialog() {
        final AlertDialogModel alertDialogModel = AlertDialogModel.builder()
                .setTitle(getString(R.string.connect_turnon_notsupported_title))
                .setMessage(getString(R.string.connect_turnon_notsupported_message))
                .setCancelable(false)
                .setNeutralButtonText(getString(android.R.string.ok))
                .build();

        AlertDialogFragment.create(alertDialogModel, decision ->
                requireActivity().finish()
        ).show(getChildFragmentManager(), TAG_NOT_SUPPORTED);
    }

    @OnClick(R.id.turnReaderOnNoDeviceAvailable)
    void onNoReaderAvailableClick() {
        if (getBaseActivity() instanceof TestActivity) {
            ((TestActivity) getBaseActivity()).showHelpScreen();
            return;
        }

        if (this instanceof TurnReaderOnConnectFragment) {
            analyticsManager.logEvent(new OnboardingNoReaderAvailable(System.currentTimeMillis() - screentime));
        }

        if (getBaseActivity() instanceof ConnectOnboardingReaderActivity) {
            ((ConnectOnboardingReaderActivity) getBaseActivity()).showHelpScreen();
            return;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void requestPermissions(@NonNull final List<String> missing) {
        requestPermissions(missing.toArray(new String[0]), REQ_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_PERMISSION) {
            final List<PermissionResult> permissionResults = Ix.zip(Arrays.asList(permissions), boxedList(grantResults), PermissionResult::create).toList();
            presenter.evaluatePermissionResults(permissionResults);
        }
    }

    @Override
    public void onDeviceChosen(@NonNull final ReaderDevice device) {
        presenter.connectTo(device);
    }

    @Override
    public void onConnectSkip() {
        // should be implemented in one special case only: when the phone detects an unfinished test after wetting.
    }

    private List<Integer> boxedList(@NonNull final int[] primitives) {
        final List<Integer> boxed = new ArrayList<>();

        for (int primitive : primitives) {
            boxed.add(primitive);
        }

        return boxed;
    }

    /**
     * Turn Reader On is meant to be skippable only in one case: when the phone detects an unfinished test after wetting.
     *
     * @return
     */
    protected boolean isSkipable() {
        return false;
    }

    @Override
    public void showConnectedToToast(String name) {
        Toast.makeText(requireActivity(), getString(R.string.connect_turnon_connected, name), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayPairedReaderNotAvailable(String paired, String found) {
        // TODO implement properly
        Toast.makeText(requireActivity(), "paired reader not available: " + paired + ", " + found, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEnableLocation() {
        AlertDialogModel dialogModel = AlertDialogModel.builder()
                .setTitle(getString(R.string.turnreaderon_location_title))
                .setMessage(getString(R.string.turnreaderon_location_message))
                .setNeutralButtonText(getString(R.string.alertdialog_button_ok))
                .setCancelable(false)
                .build();
        showAlertDialog(dialogModel, decision -> {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        });
    }

    @Override
    public void showErrorScreen(ErrorModel paramErrorModel) {
        Toast.makeText(requireActivity(), "show Error for " + paramErrorModel, Toast.LENGTH_SHORT).show();
    }

    protected void showAlertDialog(AlertDialogModel alertDialogModel, AlertDialogDecisionListener listener) {
        final Fragment fragmentByTag = getChildFragmentManager().findFragmentByTag(PKULAB_TEST_ALERT);
        if (fragmentByTag instanceof DialogFragment) {
            ((DialogFragment) fragmentByTag).dismiss();
        } else {
            AlertDialogFragment alertDialogFragment1 = AlertDialogFragment.create(alertDialogModel, listener);
            alertDialogFragment1.show(getChildFragmentManager(), PKULAB_TEST_ALERT);
        }
    }

    @Override
    public void showSyncResultsScreen(SyncProgress syncProgress) {
        container.setVisibility(View.INVISIBLE);
        syncContainer.setVisibility(View.VISIBLE);
        syncProgressBar.setProgress((int) ((syncProgress.getCurrent() + syncProgress.getFailed()) / (float) syncProgress.getTotal() * 100));
        if (syncProgress.getFailed() != 0) {
            syncText.setText(
                    String.format(Locale.getDefault(), "%d/%d (%d failed)", syncProgress.getCurrent(), syncProgress.getTotal(), syncProgress.getFailed())
            );
            syncText.setTextColor(ContextCompat.getColor(requireActivity(), R.color.applicationRed));
        } else {
            syncText.setText(String.format(Locale.getDefault(), "%d/%d", syncProgress.getCurrent(), syncProgress.getTotal()));
            syncText.setTextColor(ContextCompat.getColor(requireActivity(), R.color.applicationGreen));
        }
    }
}
