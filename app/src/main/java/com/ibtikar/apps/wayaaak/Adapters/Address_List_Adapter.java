package com.ibtikar.apps.wayaaak.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibtikar.apps.wayaaak.Models.AddressBook;
import com.ibtikar.apps.wayaaak.R;

import java.util.List;

public class Address_List_Adapter extends RecyclerView.Adapter<Address_List_Adapter.MyViewHolder> {
    Context context;
    List<AddressBook> list;
    onClickListener listener;

    public Address_List_Adapter(Context context, List<AddressBook> list, onClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_address_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.content.setText(list.get(position).getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView content;

        public MyViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.address_content_txt);
        }
    }

    public interface onClickListener {
        void onClick(AddressBook address);
    }

}

