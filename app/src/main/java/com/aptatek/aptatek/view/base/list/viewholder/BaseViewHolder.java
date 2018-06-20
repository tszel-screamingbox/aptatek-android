package com.aptatek.aptatek.view.base.list.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Base ViewHolder for list's viewholders
 * Registers the item clicks
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    /**
     * List item click listener interface
     *
     */
    public interface OnItemClickListener {

        /**
         * A list item is clicked on the list
         * @param v The parent view is clicked
         * @param position The clicked item position
         */
        void listItemClicked(View v, int position);
    }

    public BaseViewHolder(final View view, final Context context) {
        super(view);
        setClickableView(view);
        this.context = context;
    }

    protected Context context;

    private View clickableView; // The view which can be clicked on the list

    public void setClickableView(final View view){
        this.clickableView = view;
    }

    public View getClickableView() {
        return clickableView;
    }

    abstract public void bind(T data);
}