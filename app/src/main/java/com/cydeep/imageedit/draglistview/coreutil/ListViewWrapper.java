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

package com.cydeep.imageedit.draglistview.coreutil;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public interface ListViewWrapper {

    @NonNull
    ViewGroup getListView();

    @Nullable
    View getChildAt(int index);

    int getFirstVisiblePosition();

    int getLastVisiblePosition();

    int getCount();

    int getChildCount();

    int getHeaderViewsCount();

    int getPositionForView(@NonNull View view);

    @Nullable
    ListAdapter getAdapter();

    void smoothScrollBy(int distance, int duration);
}
