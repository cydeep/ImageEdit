package com.cydeep.imageedit.draglistview.dragdrop;

import android.widget.AbsListView;

import com.cydeep.imageedit.draglistview.coreutil.ListViewWrapper;


public interface DragAndDropListViewWrapper extends ListViewWrapper {

    void setOnScrollListener(AbsListView.OnScrollListener onScrollListener);

    int pointToPosition(int x, int y);

    int computeVerticalScrollOffset();

    int computeVerticalScrollExtent();

    int computeVerticalScrollRange();
}
