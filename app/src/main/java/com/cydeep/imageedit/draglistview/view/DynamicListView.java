/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cydeep.imageedit.draglistview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cydeep.imageedit.draglistview.OnInterceptTouchEventListener;
import com.cydeep.imageedit.draglistview.coreutil.TouchEventHandler;
import com.cydeep.imageedit.draglistview.dragdrop.DragAndDropHandler;

import java.util.Collection;
import java.util.HashSet;


/**
 * A {@link ListView} implementation which provides the following functionality:
 * <ul>
 * <li>Drag and drop</li>
 * <li>Swipe to dismiss</li>
 * <li>Swipe to dismiss with contextual undo</li>
 * <li>Animate addition</li>
 * </ul>
 */
public class DynamicListView extends ListView {

    private final MyOnScrollListener mMyOnScrollListener;

    private TouchEventHandler mCurrentHandlingTouchEventHandler;

    private DragAndDropHandler mDragAndDropHandler;
    private int mCount;
    private TouchEventHandler onTouchListener;
    private boolean isDrag;

    public DynamicListView(Context context) {
        this(context, null, 0);
    }

    public DynamicListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMyOnScrollListener = new MyOnScrollListener();
        super.setOnScrollListener(mMyOnScrollListener);
    }

    public void setOnTouchEvenListener(TouchEventHandler onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public void setOnScrollListener(final OnScrollListener onScrollListener) {
        mMyOnScrollListener.addOnScrollListener(onScrollListener);
    }

    public void enableDragAndDrop() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            throw new UnsupportedOperationException("Drag and drop is only supported API levels 14 and up!");
        }
        mDragAndDropHandler = new DragAndDropHandler(this);
        mDragAndDropHandler.setNotSwapItemCount(mCount);
    }

    public void setNotSwapItemCount(int count) {
        mCount = count;
    }

    @Override
    public void setAdapter(final ListAdapter adapter) {
        ListAdapter wrappedAdapter = adapter;
        super.setAdapter(wrappedAdapter);
        if (mDragAndDropHandler != null) {
            mDragAndDropHandler.setAdapter(adapter);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean result = true;
        if (onInterceptTouchEventListener != null) {
            result = onInterceptTouchEventListener.onInterceptTouchEvent(event);
        }
        if (result) {
            result = super.onInterceptTouchEvent(event);
        } else {
            super.onInterceptTouchEvent(event);
        }
        return result;
    }

    private OnInterceptTouchEventListener onInterceptTouchEventListener;

    @Override
    public boolean dispatchTouchEvent(@NonNull final MotionEvent ev) {
        if (mCurrentHandlingTouchEventHandler == null) {
            /* None of the TouchEventHandlers are actively consuming events yet. */
            boolean firstTimeInteracting = false;

            /* We don't support dragging items when there are items in the undo state. */
                /* Offer the event to the DragAndDropHandler */
            if (onTouchListener != null) {
                onTouchListener.onTouchEvent(ev);
            }
            if (mDragAndDropHandler != null) {
                mDragAndDropHandler.onTouchEvent(ev);
                firstTimeInteracting = mDragAndDropHandler.isInteracting();
                if (firstTimeInteracting) {
                    mCurrentHandlingTouchEventHandler = mDragAndDropHandler;
                }
            }
            if (firstTimeInteracting) {
                /* One of the TouchEventHandlers is now taking over control.
                   Cancel touch event handling on this DynamicListView */
                MotionEvent cancelEvent = MotionEvent.obtain(ev);
                cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                super.onTouchEvent(cancelEvent);
            }
            return firstTimeInteracting || super.dispatchTouchEvent(ev);
        } else {
            if (isDrag) {
                return onTouchEvent(ev);
            } else {
                return super.dispatchTouchEvent(ev);
            }
        }
    }


    /**
     * Starts dragging the item at given position. User must be touching this {@code DynamicListView}.
     * <p/>
     * This method does nothing if the drag and drop functionality is not enabled.
     *
     * @param position the position of the item in the adapter to start dragging. Be sure to subtract any header views.
     * @throws IllegalStateException if the user is not touching this {@code DynamicListView},
     *                               or if there is no adapter set.
     */
    public void startDragging(final int position) {
        if (mDragAndDropHandler != null) {
            isDrag = true;
            mDragAndDropHandler.startDragging(position);
        }
    }

    @Override
    public int computeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }

    @Override
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }


    @Override
    public boolean onTouchEvent(@NonNull final MotionEvent ev) {
        if (onTouchListener != null) {
            onTouchListener.onTouchEvent(ev);
        }
        if (mCurrentHandlingTouchEventHandler != null) {
            mCurrentHandlingTouchEventHandler.onTouchEvent(ev);
        }
        if (ev.getActionMasked() == MotionEvent.ACTION_UP || ev.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            mCurrentHandlingTouchEventHandler = null;
            isDrag = false;
        }
        return mCurrentHandlingTouchEventHandler != null || super.onTouchEvent(ev);
    }

    @Override
    protected void dispatchDraw(@NonNull final Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mDragAndDropHandler != null) {
            mDragAndDropHandler.dispatchDraw(canvas);
        }
    }

    public void setOnInterceptTouchEventListener(OnInterceptTouchEventListener onInterceptTouchEventListener) {
        this.onInterceptTouchEventListener = onInterceptTouchEventListener;
    }

    private class MyOnScrollListener implements OnScrollListener {

        private final Collection<OnScrollListener> mOnScrollListeners = new HashSet<>();

        @Override
        public void onScrollStateChanged(final AbsListView view, final int scrollState) {
            for (OnScrollListener onScrollListener : mOnScrollListeners) {
                onScrollListener.onScrollStateChanged(view, scrollState);
            }

        }

        @Override
        public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
            for (OnScrollListener onScrollListener : mOnScrollListeners) {
                onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }

        public void addOnScrollListener(final OnScrollListener onScrollListener) {
            mOnScrollListeners.add(onScrollListener);
        }
    }

}