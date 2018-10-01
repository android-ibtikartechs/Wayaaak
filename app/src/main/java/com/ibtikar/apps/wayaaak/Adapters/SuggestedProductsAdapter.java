package com.ibtikar.apps.wayaaak.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ibtikar.apps.wayaaak.Models.SuggestedProduct;
import com.ibtikar.apps.wayaaak.R;

import java.util.ArrayList;

public class SuggestedProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SuggestedProduct> arrayList;
    private Context context;
    private ContainerClickListener customListener;

    public SuggestedProductsAdapter(Context context,
                             ArrayList<SuggestedProduct> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.view_item_suggested_product, parent, false);
        viewHolder = new ItemViewHolder(viewItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final SuggestedProduct productListItem = arrayList.get(position);
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        if (!(productListItem.getImage().equals("") || productListItem.getImage() == null ))
        {
            Glide.with(context)
                    .load(productListItem.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemViewHolder.imMain);
        }

        itemViewHolder.tvTitle.setText(productListItem.getName());
        itemViewHolder.tvPrice.setText(productListItem.getOprice());

        if(arrayList.get(position).getOprice().isEmpty() || arrayList.get(position).getOprice().equals("0"))
        {
            itemViewHolder.oprice.setText(arrayList.get(position).getPrice());
            itemViewHolder.oprice.setTextColor(context.getResources().getColor(R.color.colorAccent));
            //holder.price.setVisibility(View.GONE);
        }
        else
        {
            itemViewHolder.price.setTextColor(context.getResources().getColor(R.color.colorPrice));
            itemViewHolder.price.setText(arrayList.get(position).getPrice());
            Log.d("TAG", "onBindViewHolder: " + arrayList.get(position).getOprice());
            //} else {
            itemViewHolder.oprice.setText(arrayList.get(position).getOprice());
            // holder.price.setText(products.get(position).getOprice()  );
            itemViewHolder.oprice.setTextColor(context.getResources().getColor(R.color.colorAccent));
            itemViewHolder.price.setPaintFlags(itemViewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        itemViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customListener.onItemClickListener(productListItem.getId().toString(),productListItem.getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    public void add(SuggestedProduct r) {
        arrayList.add(r);
        notifyItemInserted(arrayList.size()-1 );
    }

    public void addAll(ArrayList<SuggestedProduct> opResults) {
        for (SuggestedProduct result : opResults) {
            add(result);
        }
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imMain;
        TextView tvTitle,price, oprice;
        TextView tvPrice;
        ConstraintLayout constraintLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.cont_main);
            imMain = itemView.findViewById(R.id.imageView2);
            price = itemView.findViewById(R.id.product_price_txt);
            oprice = itemView.findViewById(R.id.product_offer_price_txt);
            tvTitle = itemView.findViewById(R.id.textView);
            tvPrice = itemView.findViewById(R.id.textView2);
        }
    }




    public interface ContainerClickListener {
        public void onItemClickListener(String id, String title);
    }
    public void setCustomButtonListner(ContainerClickListener listener) {
        this.customListener = listener;
    }
}
