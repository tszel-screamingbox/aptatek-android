package com.aptatek.pkulab.view.connect.scan;

import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    public static ScanDialogFragment create(final ScanListener listener, final boolean cancellable) {
        final ScanDialogFragment scanDialogFragment = new ScanDialogFragment();
        scanDialogFragment.listener = listener;
        scanDialogFragment.adapter = new ScanDeviceAdapter();
        scanDialogFragment.cancellable = cancellable;

        return scanDialogFragment;
    }

    @BindView(R.id.scanRecyclerView)
    RecyclerView recyclerView;
    @BindViews({R.id.scanProgress, R.id.scanProgressText})
    List<View> scanProgress;
    @BindView(R.id.scanCancel)
    View scanCancel;


    public interface ScanListener {
        void onDeviceChosen(@NonNull ReaderDevice device);

        void onConnectSkip();
    }

    private ScanListener listener;
    private ScanDeviceAdapter adapter;
    private boolean cancellable;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener((dialog1, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK);

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.connect_scan_dialog_width), getResources().getDimensionPixelSize(R.dimen.connect_scan_dialog_height));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return createContentView();
    }

    public void displayScanResults(@NonNull final List<ReaderDevice> devices) {
        adapter.setData(Ix.from(devices).map(readerDevice -> ScanDeviceAdapterItem.builder().setReaderDevice(readerDevice).build()).toList());
    }

    private View createContentView() {
        final View inflated = LayoutInflater.from(getActivity()).inflate(R.layout.layout_scan_dialog, null);
        ButterKnife.bind(this, inflated);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setConnectClickListener(item -> {
            if (listener != null) {
                listener.onDeviceChosen(item.getReaderDevice());
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ScanDialogItemDecoration());

        scanCancel.setVisibility(cancellable ? View.VISIBLE : View.GONE);

        return inflated;
    }

    @OnClick(R.id.scanCancel)
    void onCancel() {
        if (listener != null) {
            listener.onConnectSkip();
        }

        dismiss();
    }
}
