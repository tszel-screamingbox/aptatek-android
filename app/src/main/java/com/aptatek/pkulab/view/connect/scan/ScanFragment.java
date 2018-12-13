package com.aptatek.pkulab.view.connect.scan;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.injection.module.scan.ScanModule;
import com.aptatek.pkulab.view.connect.ConnectReaderScreen;
import com.aptatek.pkulab.view.connect.common.BaseConnectScreenFragment;
import com.aptatek.pkulab.view.connect.scan.adapter.ScanDeviceAdapter;
import com.aptatek.pkulab.view.connect.scan.adapter.ScanDeviceAdapterItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class ScanFragment extends BaseConnectScreenFragment<ScanView, ScanPresenter> implements ScanView {

    @Inject
    ScanPresenter presenter;

    @BindView(R.id.scanReaderList)
    RecyclerView rvReaders;
    @BindView(R.id.scanProgress)
    ProgressBar pbScanning;
    @BindView(R.id.scanStartScan)
    Button btnStartScan;
    @BindView(R.id.scanMtuInput)
    TextInputLayout tilMtu;
    @BindView(R.id.scanMtuSize)
    TextInputEditText etMtuSize;

    private ScanDeviceAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_scan;
    }

    @Override
    protected void initObjects(final View view) {
        adapter = new ScanDeviceAdapter();
        adapter.setConnectClickListener(adapterItem -> presenter.connect(adapterItem.getReaderDevice(), getMtuSize()));
        rvReaders.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvReaders.setAdapter(adapter);

        etMtuSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                try {
                    final int mtuSize = Integer.parseInt(s.toString());
                    //  if (mtuSize < 23 || mtuSize > 247) {
                        // etMtuSize.setError("Invalid MTU size, default will be used");
                    //  } else {
                        etMtuSize.setError(null);
                    // }
                } catch (final NumberFormatException ex) {
                    Timber.d("Failed to convert mtu size [%s] to number", s.toString());
                    etMtuSize.setError("Invalid MTU size, default will be used");
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });
    }

    private int getMtuSize() {
        try {
            final int mtuSize = Integer.parseInt(etMtuSize.getText().toString());
            return mtuSize;
        } catch (final NumberFormatException e) {
            return LumosReaderConstants.MTU_SIZE;
        }
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.plus(new ScanModule()).inject(this);
    }

    @NonNull
    @Override
    public ScanPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.startScan();
    }

    @Override
    public void onStop() {
        presenter.stopScan();

        super.onStop();
    }

    @Override
    public void displayScanResults(@NonNull final List<ScanDeviceAdapterItem> devices) {
        adapter.setData(devices);
    }

    @Override
    public void showLoading(final boolean loading) {
        pbScanning.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
        btnStartScan.setEnabled(!loading);
        btnStartScan.setText(loading ? R.string.connect_scan_in_progress : R.string.connect_scan_start);
    }

    @Override
    public void showConnected(@NonNull final ReaderDevice readerDevice) {
        showScreen(ConnectReaderScreen.CONNECTED);
    }

    @Override
    public void showErrorToast(@NonNull final String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMtuSizeChanged(final int mtuSize) {
        Toast.makeText(getActivity(), "MTU size changed to: " + mtuSize, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMtuError() {
        Toast.makeText(getActivity(), "Failed to set MTU size", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.scanStartScan)
    public void onClickScan() {
        presenter.startScan();
    }
}
