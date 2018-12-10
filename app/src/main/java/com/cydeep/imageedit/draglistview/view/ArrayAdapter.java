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
import android.support.annotation.NonNull;
import android.widget.BaseAdapter;

import com.cydeep.imageedit.base.ListViewAdapter;
import com.cydeep.imageedit.base.OnUpdateListUIListener;
import com.cydeep.imageedit.draglistview.coreutil.Insertable;
import com.cydeep.imageedit.draglistview.coreutil.Swappable;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public class ArrayAdapter<T> extends ListViewAdapter implements Swappable, Insertable<T> {

    private BaseAdapter mDataSetChangedSlavedAdapter;

    public ArrayAdapter(Context context, OnUpdateListUIListener onUpdateUIListener) {
        super(context,onUpdateUIListener);
    }


    @Override
    public long getItemId(final int position) {
        return getItem(position).hashCode();
    }

    @Override
    @NonNull
    public T getItem(final int position) {
        return getItems().get(position);
    }

    /**
     * Returns the items.
     */
    @NonNull
    public List<T> getItems() {
        return onUpdateUIListener.getData();
    }

    /**
     * Appends the specified element to the end of the {@code List}.
     *
     * @param object the object to add.
     *
     * @return always true.
     */
    public boolean add(@NonNull final T object) {
        boolean result = onUpdateUIListener.getData().add(object);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public void add(final int index, @NonNull final T item) {
        onUpdateUIListener.getData().add(index, item);
        notifyDataSetChanged();
    }

    /**
     * Adds the objects in the specified collection to the end of this List. The objects are added in the order in which they are returned from the collection's iterator.
     *
     * @param collection the collection of objects.
     *
     * @return {@code true} if this {@code List} is modified, {@code false} otherwise.
     */
    public boolean addAll(@NonNull final Collection<? extends T> collection) {
        boolean result = onUpdateUIListener.getData().addAll(collection);
        notifyDataSetChanged();
        return result;
    }

    public boolean contains(final T object) {
        return onUpdateUIListener.getData().contains(object);
    }

    public void clear() {
        onUpdateUIListener.getData().clear();
        notifyDataSetChanged();
    }

    public boolean remove(@NonNull final Object object) {
        boolean result = onUpdateUIListener.getData().remove(object);
        notifyDataSetChanged();
        return result;
    }

    @NonNull
    public T remove(final int location) {
        T result = getItems().remove(location);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public void swapItems(final int positionOne, final int positionTwo) {
        T firstItem = getItems().set(positionOne, getItem(positionTwo));
        notifyDataSetChanged();
        getItems().set(positionTwo, firstItem);
    }

    public void propagateNotifyDataSetChanged(@NonNull final BaseAdapter slavedAdapter) {
        mDataSetChangedSlavedAdapter = slavedAdapter;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (mDataSetChangedSlavedAdapter != null) {
            mDataSetChangedSlavedAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
