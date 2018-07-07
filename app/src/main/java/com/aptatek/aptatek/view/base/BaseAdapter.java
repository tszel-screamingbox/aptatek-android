package com.aptatek.aptatek.view.base;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, AI extends AdapterItem> extends RecyclerView.Adapter<VH> {

    protected final AsyncListDiffer<AI> data =
            new AsyncListDiffer<>(this,
                    new DiffUtil.ItemCallback<AI>() {
                        @Override
                        public boolean areItemsTheSame(final AI oldItem, final AI newItem) {
                            return oldItem.uniqueIdentifier().equals(newItem.uniqueIdentifier());
                        }

                        @Override
                        public boolean areContentsTheSame(final AI oldItem, final AI newItem) {
                            return oldItem.equals(newItem);
                        }
                    });

    public void setData(final List<AI> mData) {
        data.submitList(mData);
    }

    public List<AI> getData() {
        return data.getCurrentList();
    }

    protected abstract VH setViewHolder(@NonNull ViewGroup parent, int viewType);

    protected abstract void bindData(@NonNull VH holder, int position);

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return setViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        bindData(holder, position);
    }

    @Override
    public int getItemCount() {
        return data.getCurrentList().size();
    }
}
