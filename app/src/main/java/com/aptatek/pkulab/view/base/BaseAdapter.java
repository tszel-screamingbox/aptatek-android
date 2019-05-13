package com.aptatek.pkulab.view.base;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;

import java.util.List;

public abstract class BaseAdapter<V extends RecyclerView.ViewHolder, I extends AdapterItem> extends RecyclerView.Adapter<V> {

    protected final AsyncListDiffer<I> data =
        new AsyncListDiffer<>(this,
            new DiffUtil.ItemCallback<I>() {
                @Override
                public boolean areItemsTheSame(final I oldItem, final I newItem) {
                    return oldItem.uniqueIdentifier().equals(newItem.uniqueIdentifier());
                }

                @Override
                public boolean areContentsTheSame(final I oldItem, final I newItem) {
                    return oldItem.equals(newItem);
                }
            });

    public void setData(final List<I> mData) {
        data.submitList(mData);
    }

    public List<I> getData() {
        return data.getCurrentList();
    }

    protected abstract V setViewHolder(@NonNull ViewGroup parent, int viewType);

    protected abstract void bindData(@NonNull V holder, int position);

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return setViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull final V holder, final int position) {
        bindData(holder, position);
    }

    @Override
    public int getItemCount() {
        return data.getCurrentList().size();
    }
}
