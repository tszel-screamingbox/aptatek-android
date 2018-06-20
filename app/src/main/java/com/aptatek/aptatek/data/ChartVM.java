package com.aptatek.aptatek.data;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.view.base.list.IListTypeProvider;

public class ChartVM implements IListTypeProvider {

    public int height;
    public int width;

    public ChartVM(final int height, final int width) {
        this.width = width;
        this.height = height;
    }


    @Override
    public int getLayoutType() {
        return R.layout.item_chart;
    }
}
