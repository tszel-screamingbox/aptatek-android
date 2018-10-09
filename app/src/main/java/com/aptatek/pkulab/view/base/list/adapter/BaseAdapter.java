package com.aptatek.pkulab.view.base.list.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aptatek.pkulab.view.base.list.IListTypeProvider;
import com.aptatek.pkulab.view.base.list.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BaseAdapter for handling data in lists
 */
@SuppressWarnings("unused")
//TODO: merge with the another adapter
public abstract class BaseAdapter<T extends IListTypeProvider>
        extends RecyclerView.Adapter<BaseViewHolder<T>>
        implements View.OnClickListener {

    protected Context context;
    protected List<T> items;
    private BaseViewHolder.OnItemClickListener onClickListener;

    /**
     * Default constructor
     */
    public BaseAdapter(final Context context) {
        this.context = context;
        items = new ArrayList<>();
    }

    /**
     * Constructor with pre initialized list
     *
     * @param context The context
     * @param items   The new items
     */
    public BaseAdapter(final Context context, @NonNull final List<T> items) {
        this.context = context;
        this.items = items;
    }

    /**
     * Costructor with pre initialized list and onclicklistener
     *
     * @param context         The context
     * @param items           The new items
     * @param onClickListener Item onclicklistenr
     */
    public BaseAdapter(final Context context,
                       @NonNull final List<T> items,
                       final BaseViewHolder.OnItemClickListener onClickListener) {
        this.context = context;
        this.items = items;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemViewType(final int position) {
        return getItem(position).getLayoutType();
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder<T> holder, final int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    /**
     * Get item from the list via position
     *
     * @param position The item's position
     * @return Type of item
     */
    public T getItem(final int position) {
        return items == null ? null : items.get(position);
    }

    /**
     * Get the position of the given item
     *
     * @param item The item
     * @return The position of the item if is in the collection, -1 otherwise
     */
    public int getItemPosition(final T item) {
        if (items.contains(item)) {
            return items.indexOf(item);
        } else {
            return -1;
        }
    }

    /**
     * Get all items
     *
     * @return
     */
    public List<T> getItems() {
        return items == null ? Collections.emptyList() : items;
    }

    /**
     * Set a new set of items
     *
     * @param items The new items
     */
    public void setItems(final List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    /**
     * Add new item to the end of the list
     *
     * @param item The new item
     */
    public void addItem(final T item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    /**
     * Add new item to the list to the specified position
     *
     * @param position The position to insert
     * @param item     The new item
     */
    public void addItem(final int position, final T item) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * Add a list to the end of the existing list
     *
     * @param items The new list of items
     */
    public void addItems(final List<T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * Remove the given item from the list
     *
     * @param item The removable item
     */
    public void removeItem(final T item) {
        if (items.contains(item)) {
            final int position = items.indexOf(item);
            items.remove(item);
            notifyItemRemoved(position);
        }
    }

    /**
     * Remove item via it's position in the list
     *
     * @param position The item's position
     */
    public void removeItem(final int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Remove all items from the list
     */
    public void removeAllItems() {
        final int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    /**
     * Update a single item in the list
     *
     * @param oldItem the item you want to update
     * @param newItem the item you want to insert in place of the old one
     */
    public void updateItem(final T oldItem, final T newItem) {
        if (items.contains(oldItem)) {
            final int position = items.indexOf(oldItem);
            items.set(position, newItem);
            notifyItemChanged(position);
        }
    }

    /**
     * Notify item is changed in the list
     *
     * @param item The changed item
     */
    public void notifyItemChanged(final T item) {
        if (items.contains(item)) {
            notifyItemChanged(items.indexOf(item));
        }
    }

    /**
     * Prepares the selected view for handling the tap if it was set before
     *
     * @param vh The actual viewholder
     */
    protected void prepareItemOnClick(final BaseViewHolder vh) {
        final View view = vh.getClickableView();
        if (view != null) {
            view.setOnClickListener(this);
            view.setTag(vh);
        }
    }

    @Override
    public void onClick(final View v) {
        if (onClickListener != null) {
            final BaseViewHolder vh = (BaseViewHolder) v.getTag();
            onClickListener.listItemClicked(v, vh.getLayoutPosition());
        }
    }

    public BaseViewHolder.OnItemClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(final BaseViewHolder.OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Context getContext() {
        return context;
    }
}
