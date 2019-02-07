package com.aptatek.pkulab.view.connect.turnreaderon;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.widget.HeaderView;
import com.mklimek.frameviedoview.FrameVideoView;
import com.mklimek.frameviedoview.FrameVideoViewListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public abstract class TurnReaderOnFragment<V extends TurnReaderOnView, P extends TurnReaderOnPresenter<V>> extends BaseFragment<V, P> implements TurnReaderOnView {

    @BindView(R.id.header)
    protected HeaderView titleHeaderView;
    @BindView(R.id.testVideo)
    protected FrameVideoView videoView;

    @Inject
    ResourceInteractor resourceInteractor;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_video;
    }

    @Override
    protected void initObjects(final View view) {
        titleHeaderView.setTitle(resourceInteractor.getStringResource(R.string.test_turnreaderon_title));
        titleHeaderView.setSubtitle(resourceInteractor.getStringResource(R.string.test_turnreaderon_message));
        playVideo(resourceInteractor.getUriForRawFile(R.raw.turn_reader_on), true);
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
    public void displayMissingPermissions() {
        Toast.makeText(getActivity(), "displayMissingPermissions", Toast.LENGTH_SHORT).show();
        // TODO display permission screen
    }

    @Override
    public void displayReaderSelector(@NonNull List<ReaderDevice> readerDevices) {
        Toast.makeText(getActivity(), "displayReaderSelector", Toast.LENGTH_SHORT).show();
        // TODO display dialog
    }

    @Override
    public void displaySelfCheckAnimation() {
        Toast.makeText(getActivity(), "displaySelfCheckAnimation", Toast.LENGTH_SHORT).show();
        playVideo(resourceInteractor.getUriForRawFile(R.raw.self_check), true);
    }

    @Override
    public void onSelfCheckComplete() {
        Toast.makeText(getActivity(), "onSelfCheckComplete", Toast.LENGTH_SHORT).show();
        // TODO next screen
    }

    @Override
    public void displayNoReaderAvailable() {
        Toast.makeText(getActivity(), "displayNoReaderAvailable", Toast.LENGTH_SHORT).show();
        // TODO display FAQ button
    }
}
