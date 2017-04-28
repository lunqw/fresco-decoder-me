package com.yy.lqw.fresco.sample;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lunqingwen on 2017/4/23.
 */

public class InputAdapter extends Adapter<InputAdapter.EntryHolder> {
    private List<String> mItems;
    private OnItemClickListener mOnItemClickListener;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mItems = new ArrayList<>();

        final TypedArray resArray = recyclerView.getResources()
                .obtainTypedArray(R.array.input_entries_res);
        for (int i = 0; i < resArray.length(); i++) {
            int resId = resArray.getResourceId(i, 0);
            mItems.add("res:/" + resId);
        }

        final String[] strArray = recyclerView.getResources()
                .getStringArray(R.array.input_entries_str);
        mItems.addAll(Arrays.asList(strArray));
        resArray.recycle();
    }

    @Override
    public EntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntryHolder(new Button(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final EntryHolder holder, int position) {
        ((Button) holder.itemView).setText(String.valueOf(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    final int pos = holder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, mItems.get(pos), pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    class EntryHolder extends ViewHolder {
        public EntryHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, String item, int position);
    }
}
