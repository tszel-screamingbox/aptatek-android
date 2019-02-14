package com.aptatek.pkulab.view.connect.turnreaderon;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.scan.ScanDialogFragment;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.test.TestActivity;
import com.aptatek.pkulab.widget.HeaderView;
import com.mklimek.frameviedoview.FrameVideoView;
import com.mklimek.frameviedoview.FrameVideoViewListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ix.Ix;

public abstract class TurnReaderOnFragment<V extends TurnReaderOnView, P extends TurnReaderOnPresenter<V>> extends BaseFragment<V, P> implements TurnReaderOnView {

    private static final String TAG_SCAN = "pkulab.scan.devices";
    private static final String TAG_NOT_SUPPORTED = "pkulab.scan.devicenotsupported";
    private static final int REQ_PERMISSION = 737;

    @BindView(R.id.header)
    protected HeaderView headerView;
    @BindView(R.id.testVideo)
    protected FrameVideoView videoView;
    @BindView(R.id.turnReaderOnNoDeviceAvailable)
    protected Button noReaderAvailable;

    @Inject
    ResourceInteractor resourceInteractor;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_turnreaderon;
    }

    @Override
    protected void initObjects(final View view) {
        headerView.setTitle(resourceInteractor.getStringResource(R.string.test_turnreaderon_title));
        headerView.setSubtitle(resourceInteractor.getStringResource(R.string.test_turnreaderon_message));
        playVideo(resourceInteractor.getUriForRawFile(R.raw.turn_reader_on), true);

        if (getActivity() instanceof TestActivity) {
            final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) noReaderAvailable.getLayoutParams();
            layoutParams.bottomMargin += resourceInteractor.getDimension(R.dimen.test_bottombar_height);
        }
    }

    protected void playVideo(@NonNull final Uri uri, final boolean shouldLoop) {
        videoView.setup(uri, ContextCompat.getColor(getActivity(), R.color.applicationWhite));
        videoView.setFrameVideoViewListener(new FrameVideoViewListener() {
            @Override
            public void mediaPlayerPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(shouldLoop);
                mediaPlayer.start();
            }

            @Override
            public void mediaPlayerPrepareFailed(MediaPlayer mediaPlayer, String s) {

            }
        });
    }

    @Override
    public void onStop() {
        videoView.setFrameVideoViewListener(null);

        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        videoView.onResume();
        presenter.onResumed();
    }

    @Override
    public void onPause() {
        videoView.onPause();
        presenter.onPaused();

        super.onPause();
    }

    @Override
    public void displayReaderSelector(@NonNull final List<ReaderDevice> readerDevices) {
        ScanDialogFragment scanDialogFragment = findScanDialogFragment();
        if (scanDialogFragment == null) {
            scanDialogFragment = ScanDialogFragment.create(
                    new ScanDialogFragment.ScanListener() {
                        @Override
                        public void onConnectTo(@NonNull final ReaderDevice device) {
                            presenter.connectTo(device);
                        }

                        @Override
                        public void onCancelled() {
                            Toast.makeText(getActivity(), "what to do now?", Toast.LENGTH_SHORT).show();
                        }
                    }
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
        playVideo(resourceInteractor.getUriForRawFile(R.raw.self_check), true);
    }

    @Override
    public void onSelfCheckComplete() {
        Toast.makeText(getActivity(), "onSelfCheckComplete", Toast.LENGTH_SHORT).show();
        // TODO next screen
    }

    @Override
    public void displayNoReaderAvailable() {
        noReaderAvailable.setVisibility(View.VISIBLE);
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
                getActivity().finish()
        ).show(getChildFragmentManager(), TAG_NOT_SUPPORTED);
    }

    @OnClick(R.id.turnReaderOnNoDeviceAvailable)
    void onNoReaderAvailableClick() {
        Toast.makeText(getActivity(), "TODO: handle click", Toast.LENGTH_SHORT).show();
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

    private List<Integer> boxedList(@NonNull final int[] primitives) {
        final List<Integer> boxed = new ArrayList<>();

        for (int primitive : primitives) {
            boxed.add(primitive);
        }

        return boxed;
    }
}
