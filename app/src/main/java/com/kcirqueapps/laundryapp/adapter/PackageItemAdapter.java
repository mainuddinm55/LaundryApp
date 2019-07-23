package com.kcirqueapps.laundryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.network.Model.packageresponse.Item;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PackageItemAdapter extends RecyclerView.Adapter<PackageItemAdapter.PackageItemHolder> {
    private List<Item> itemList = new ArrayList<>();

    @NonNull
    @Override
    public PackageItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PackageItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PackageItemHolder holder, int position) {
        if (position == itemList.size() - 1) {
            holder.dividerView.setVisibility(View.GONE);
        }

        Item item = itemList.get(position);
        holder.bindTo(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    class PackageItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView serviceNameTextView, collectionDateTextView, deliveryDateTextView, quantityTextView, statusTextView;
        private final CircleImageView serviceImageView;
        private final View dividerView;

        PackageItemHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTextView = itemView.findViewById(R.id.service_name_text_view);
            collectionDateTextView = itemView.findViewById(R.id.collection_date_text_view);
            deliveryDateTextView = itemView.findViewById(R.id.service_type_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            serviceImageView = itemView.findViewById(R.id.service_image_view);
            dividerView = itemView.findViewById(R.id.divider_view);
            statusTextView = itemView.findViewById(R.id.amount_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        void bindTo(Item item) {
            serviceNameTextView.setText(item.getCloth());
            collectionDateTextView.setText(String.format("%s %s", itemView.getContext().getString(R.string.collection_text), item.getCollectionDate()));
            deliveryDateTextView.setText(String.format("%s %s", itemView.getContext().getString(R.string.delivery_text), item.getDeliveryDate()));
            quantityTextView.setText(String.format("%s %s", item.getUnit(), itemView.getContext().getString(R.string.unit_text)));
            Glide.with(itemView).load(item.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(serviceImageView);
            statusTextView.setText(item.getDeliveryStatus());
            collectionDateTextView.setVisibility(View.VISIBLE);
        }
    }
}
