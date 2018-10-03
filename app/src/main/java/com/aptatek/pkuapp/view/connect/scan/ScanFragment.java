package com.aptatek.pkuapp.view.connect.scan;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.model.ReaderDevice;
import com.aptatek.pkuapp.injection.component.FragmentComponent;
import com.aptatek.pkuapp.injection.module.connect.ConnectModule;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenFragment;
import com.aptatek.pkuapp.view.connect.scan.adapter.ScanDeviceAdapter;
import com.aptatek.pkuapp.view.connect.scan.adapter.ScanDeviceAdapterItem;

import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ix.Ix;

public class ScanFragment extends BaseConnectScreenFragment<ScanView, ScanPresenter> implements ScanView {

    @Inject
    ScanPresenter presenter;

    @BindView(R.id.scanReaderList)
    RecyclerView rvReaders;
    @BindView(R.id.scanProgress)
    ProgressBar pbScanning;
    @BindView(R.id.scanStartScan)
    Button btnStartScan;

    private ScanDeviceAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_scan;
    }

    @Override
    protected void initObjects(final View view) {
        adapter = new ScanDeviceAdapter();
        rvReaders.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvReaders.setAdapter(adapter);
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.plus(new ConnectModule()).inject(this);
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
    public void displayScanResults(@NonNull final Set<ReaderDevice> devices) {
        adapter.setData(Ix.from(devices).map(ScanDeviceAdapterItem::create).toList());
    }

    @Override
    public void showLoading(final boolean loading) {
        pbScanning.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
        btnStartScan.setEnabled(!loading);
        btnStartScan.setText(loading ? R.string.connect_scan_in_progress : R.string.connect_scan_start);
    }

    @OnClick(R.id.scanStartScan)
    public void onClickScan() {
        presenter.startScan();
    }
}
