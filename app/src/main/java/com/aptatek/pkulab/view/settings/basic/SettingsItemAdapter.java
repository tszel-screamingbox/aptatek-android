package com.aptatek.pkulab.view.settings.basic;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.settings.SettingsFingerprintAuth;
import com.aptatek.pkulab.view.base.BaseAdapter;

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
    private IAnalyticsManager analyticsManager;

    @Inject
    public SettingsItemAdapter(final IAnalyticsManager analyticsManager) {
        this.analyticsManager = analyticsManager;
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
                if (settingsAdapterItem.getSettingsItem() == SettingsItem.FINGERPRINT_AUTH && settingsAdapterItem.isEnabled()) {
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
                    analyticsManager.logEvent(new SettingsFingerprintAuth(isChecked));
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
