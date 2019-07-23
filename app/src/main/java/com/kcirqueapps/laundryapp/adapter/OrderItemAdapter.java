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
import com.kcirqueapps.laundryapp.network.Model.packageresponse.PackageOrderResponse;
import com.kcirqueapps.laundryapp.network.Model.orderresponse.Item;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemHolder> {
    private List<Object> orderItemList = new ArrayList<>();
    private com.kcirqueapps.laundryapp.network.Model.packageresponse.Order order;
    private ItemClickListener itemClickListener;

    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder holder, int position) {
        Object object = orderItemList.get(position);
        if (position == orderItemList.size() - 1) {
            holder.dividerView.setVisibility(View.GONE);
        }
        if (object instanceof Item) {
            Item item = (Item) object;
            holder.bindToServiceItem(item);
        }
        if (object instanceof com.kcirqueapps.laundryapp.network.Model.packageresponse.Item) {
            com.kcirqueapps.laundryapp.network.Model.packageresponse.Item item = (com.kcirqueapps.laundryapp.network.Model.packageresponse.Item) object;
            holder.bindToPackageItem(item);
        }
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public void setOrderItemList(List<Object> orderItemList) {
        this.orderItemList = orderItemList;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOrderItemList(List<Object> orderItemList, com.kcirqueapps.laundryapp.network.Model.packageresponse.Order order) {
        this.orderItemList = orderItemList;
        this.order = order;
        notifyDataSetChanged();
    }

    class OrderItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView serviceNameTextView, serviceTypeTextView, quantityTextView, amountTextView;
        private final CircleImageView serviceImageView;
        private final View dividerView;

        OrderItemHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTextView = itemView.findViewById(R.id.service_name_text_view);
            serviceTypeTextView = itemView.findViewById(R.id.service_type_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            serviceImageView = itemView.findViewById(R.id.service_image_view);
            dividerView = itemView.findViewById(R.id.divider_view);
            amountTextView = itemView.findViewById(R.id.amount_text_view);
            itemView.setOnClickListener(this);
        }

        void bindToServiceItem(Item item) {
            serviceNameTextView.setText(item.getCloth());
            serviceTypeTextView.setText(item.getService());
            quantityTextView.setText(String.format("%s %s", item.getUnit(), itemView.getContext().getString(R.string.unit_text)));
            Glide.with(itemView).load(item.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(serviceImageView);
            amountTextView.setText(String.format("%s %s", itemView.getContext().getString(R.string.tk_sign), item.getAmount()));
        }

        void bindToPackageItem(com.kcirqueapps.laundryapp.network.Model.packageresponse.Item item) {
            serviceNameTextView.setText(item.getCloth());
            serviceTypeTextView.setText(order.getServiceType());
            quantityTextView.setText(String.format("%s %s", item.getUnit(), itemView.getContext().getString(R.string.unit_text)));
            Glide.with(itemView).load(item.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(serviceImageView);
            amountTextView.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onItemClicked();
        }
    }

    public interface ItemClickListener {
        void onItemClicked();
    }
}
