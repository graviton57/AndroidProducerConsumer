package com.havrylyuk.producerconsumer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor Havrylyuk on 29.03.2017.
 */

public class ProducerConsumerAdapter extends RecyclerView.Adapter<ProducerConsumerAdapter.ItemHolder> {


    public interface ItemClickListener {
        void onItemClick(long id);
    }

    private ItemClickListener listener;
    private List<String> list;


    public ProducerConsumerAdapter(ItemClickListener listener) {
        this.listener = listener;
        this.list = new ArrayList<>();
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addItem(String item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int position) {
        final int id =position;
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(id);
                }
            }
        });
        String info = list.get(position);
        holder.info.setText(info);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        private  View view;
        private  TextView info;

        public ItemHolder(View view) {
            super(view);
            this.view = view;
            this.info = (TextView) view.findViewById(R.id.list_item_name);
        }
    }
}
