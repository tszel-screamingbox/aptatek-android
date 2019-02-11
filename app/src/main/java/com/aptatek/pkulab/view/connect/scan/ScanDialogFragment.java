package com.aptatek.pkulab.view.connect.scan;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.view.connect.scan.adapter.ScanDeviceAdapter;
import com.aptatek.pkulab.view.connect.scan.adapter.ScanDeviceAdapterItem;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ix.Ix;

public class ScanDialogFragment extends DialogFragment {

    private ScanDeviceAdapter adapter;

    public static ScanDialogFragment create(final ScanListener listener) {
        final ScanDialogFragment scanDialogFragment = new ScanDialogFragment();
        scanDialogFragment.listener = listener;

        return scanDialogFragment;
    }

    @BindView(R.id.scanRecyclerView)
    RecyclerView recyclerView;
    @BindViews({R.id.scanProgress, R.id.scanProgressText})
    List<View> scanProgress;


    public interface ScanListener {
        void onConnectTo(@NonNull ReaderDevice device);
        void onCancelled();
    }

    private ScanListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(createContentView());

        return dialog;
    }

    public void displayScanResults(@NonNull final List<ReaderDevice> devices) {
        adapter.setData(Ix.from(devices).map(readerDevice -> ScanDeviceAdapterItem.builder().setReaderDevice(readerDevice).build()).toList());
    }

    private View createContentView() {
        final View inflated = LayoutInflater.from(getActivity()).inflate(R.layout.layout_scan_dialog, null);
        ButterKnife.bind(this, inflated);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ScanDeviceAdapter();
        adapter.setConnectClickListener(item -> {
            if (listener != null) {
                listener.onConnectTo(item.getReaderDevice());
            }

            dismiss();
        });
        recyclerView.setAdapter(adapter);

        return inflated;
    }

    @OnClick(R.id.scanCancel)
    void onCancel() {
        if (listener != null) {
            listener.onCancelled();
        }

        dismiss();
    }
}
