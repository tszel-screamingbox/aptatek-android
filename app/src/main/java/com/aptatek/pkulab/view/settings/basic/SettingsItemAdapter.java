package com.aptatek.pkulab.view.settings.basic;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.view.base.BaseAdapter;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ix.Ix;


public class SettingsItemAdapter extends BaseAdapter<RecyclerView.ViewHolder, SettingsAdapterItem> {

    private static final int VIEW_TYPE_FINGERPRINT = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    interface SettingsItemClickListener {

        void onFingerprintAuthToggled(final boolean isChecked);

        void onSettingsItemClicked(final SettingsItem item);

    }

    private SettingsItemClickListener settingsItemClickListener;

    @Inject
    public SettingsItemAdapter() {
        setData(Ix.from(Arrays.asList(SettingsItem.values()))
                .map(settingsItem -> new SettingsAdapterItem(settingsItem, false, false))
                .toList()
        );
    }

    public void setSettingsItemClickListener(@Nullable final SettingsItemClickListener settingsItemClickListener) {
        this.settingsItemClickListener = settingsItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (getData().get(position).getSettingsItem() == SettingsItem.FINGERPRINT_AUTH) {
            return VIEW_TYPE_FINGERPRINT;
        }

        return VIEW_TYPE_OTHER;
    }

    @Override
    protected RecyclerView.ViewHolder setViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        if (viewType == VIEW_TYPE_FINGERPRINT) {
            return new FingerprintItemViewHolder(parent);
        }

        return new SettingsItemViewHolder(parent);
    }

    @Override
    protected void bindData(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final SettingsAdapterItem settingsAdapterItem = getData().get(position);
        if (holder instanceof SettingsItemViewHolder) {
            ((SettingsItemViewHolder) holder).bind(settingsAdapterItem.getSettingsItem());
        } else if (holder instanceof FingerprintItemViewHolder) {
            ((FingerprintItemViewHolder) holder).bind(settingsAdapterItem);
        }

        holder.itemView.setOnClickListener(v -> {
            if (settingsItemClickListener != null) {
                if (settingsAdapterItem.getSettingsItem() == SettingsItem.FINGERPRINT_AUTH) {
                    settingsItemClickListener.onFingerprintAuthToggled(!settingsAdapterItem.isChecked());
                } else {
                    settingsItemClickListener.onSettingsItemClicked(settingsAdapterItem.getSettingsItem());
                }
            }
        });

        if (holder instanceof FingerprintItemViewHolder) {
            ((FingerprintItemViewHolder) holder).switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (settingsItemClickListener != null) {
                    settingsItemClickListener.onFingerprintAuthToggled(isChecked);
                }
            });
        }
    }

    public void updateFingerprintItem(final boolean isEnabled, final boolean isChecked) {
        setData(Ix.from(getData()).map(item -> {
                    if (item.getSettingsItem() == SettingsItem.FINGERPRINT_AUTH) {
                        return new SettingsAdapterItem(item.getSettingsItem(), isEnabled, isChecked);
                    }

                    return item;
                }).toList()
        );
    }

    static class FingerprintItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.switchFingerprint)
        SwitchCompat switchCompat;

        FingerprintItemViewHolder(final ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings_fingerprint, parent, false));
            ButterKnife.bind(this, itemView);
        }

        void bind(final SettingsAdapterItem item) {
            switchCompat.setEnabled(item.isEnabled());
            switchCompat.setChecked(item.isChecked());
        }

    }

    static class SettingsItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.settings_item_title)
        TextView tvTitle;

        @BindView(R.id.settings_item_description)
        TextView tvDescription;

        SettingsItemViewHolder(final ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings, parent, false));
            ButterKnife.bind(this, itemView);
        }

        void bind(final SettingsItem item) {
            if (item.getTitleRes() != 0) {
                tvTitle.setText(item.getTitleRes());
            }
            if (item.getDescriptionRes() != 0) {
                tvDescription.setVisibility(View.VISIBLE);
                tvDescription.setText(item.getDescriptionRes());
            } else {
                tvDescription.setVisibility(View.GONE);
            }
        }

    }

}
