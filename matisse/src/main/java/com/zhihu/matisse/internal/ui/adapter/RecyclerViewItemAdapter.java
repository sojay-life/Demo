/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhihu.matisse.internal.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.entity.SelectionSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewItemAdapter<VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<VH> {

    private Cursor mCursor;
    private List<Item> itemList;
    private Context contex;

    RecyclerViewItemAdapter(Context contex, Cursor c) {
        setHasStableIds(true);
        swapCursor(c);
        this.contex = contex;
    }

    protected abstract void onBindViewHolder(VH holder, Item cursor);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, itemList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, itemList.get(position));
    }

    protected abstract int getItemViewType(int position, Item cursor);

    @Override
    public int getItemCount() {
        if (itemList != null) {
            return itemList.size();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return itemList.get(position).id;
    }

    public void swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return;
        }
        if (itemList == null)
            itemList = new ArrayList<>();
        else
            itemList.clear();
        if (newCursor != null) {
            mCursor = newCursor;
            if (mCursor.moveToFirst()) {//判断数据表里有数据
                do {//遍历数据表中的数据
                    if (SelectionSpec.getInstance().mimeTypeSet.contains(MimeType.MP4)) {  //判断视频时间长度是否符合要求
                        Long time = mCursor.getLong(mCursor.getColumnIndex("duration"));//通过列名“　”获取该列索引，再根据索引获取对应的数据。
                        if (time / 1000 >= SelectionSpec.getInstance().videoMinSize && time / 1000 <= SelectionSpec.getInstance().videoMaxSize) {
                            addItem(mCursor);
                        }
                    } else if (MimeType.ofNotGif().containsAll(SelectionSpec.getInstance().mimeTypeSet)) {                                                              //可在此增加图片等判断
                        if (!MimeType.isGif(Item.valueOf(mCursor).mimeType)) {
                            addItem(mCursor);
                        }
                    } else {
                        addItem(mCursor);
                    }
                } while (mCursor.moveToNext());
                mCursor.close();
            }

            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
            mCursor = null;
        }
    }

    /**
     * 增加忽略已被删除的数据
     *
     * @param mCursor
     */
    private void addItem(Cursor mCursor) {
        Item item = Item.valueOf(mCursor);
        if (item.id == -1)
            itemList.add(item);
        else {
            String path = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            if (fileExist(path))
                itemList.add(item);
        }
    }

    private boolean fileExist(String path) {
        if (!TextUtils.isEmpty(path)) {
            return new File(path).exists();
        }
        return false;
    }

    public Cursor getCursor() {
        return mCursor;
    }

    private boolean isDataValid(Cursor cursor) {
        return cursor != null && !cursor.isClosed();
    }
}
