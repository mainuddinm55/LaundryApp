package com.kcirqueapps.laundryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.network.Model.packageresponse.PackageOrderResponse;
import com.kcirqueapps.laundryapp.network.Model.orderresponse.ServiceOrderResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private List<Object> orderList = new ArrayList<>();
    private ItemClickListener itemClickListener;

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        Object object = orderList.get(position);
        if (object instanceof ServiceOrderResponse) {
            ServiceOrderResponse orderResponse = (ServiceOrderResponse) object;
            holder.bindServiceOrder(orderResponse);
        } else if (object instanceof PackageOrderResponse) {
            PackageOrderResponse response = (PackageOrderResponse) object;
            holder.bindPackageService(response);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setOrderList(List<Object> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OrderItemAdapter.ItemClickListener {
        private final TextView orderIdTextView, orderDateTextView, statusTextView;
        private final RecyclerView itemRecyclerView;

        OrderHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.order_id_text_view);
            orderDateTextView = itemView.findViewById(R.id.order_date_text_view);
            statusTextView = itemView.findViewById(R.id.status_text_view);
            itemRecyclerView = itemView.findViewById(R.id.item_recycler_view);
            itemView.setOnClickListener(this);
        }


        void bindServiceOrder(ServiceOrderResponse orderResponse) {
            List<Object> itemList = new ArrayList<Object>(orderResponse.getItems());
            orderIdTextView.setText(String.format(Locale.ENGLISH, "%s%d", itemView.getContext().getString(R.string.order_text), orderResponse.getOrder().getId()));
            orderDateTextView.setText(orderResponse.getOrder().getCollectionDate());
            statusTextView.setText(orderResponse.getOrder().getDeliveryStatus());
            itemRecyclerView.setHasFixedSize(true);
            OrderItemAdapter adapter = new OrderItemAdapter();
            itemRecyclerView.setAdapter(adapter);
            adapter.setOrderItemList(itemList);
        }

        void bindPackageService(PackageOrderResponse orderResponse) {
            List<Object> itemList = new ArrayList<Object>(orderResponse.getItems());
            orderIdTextView.setText(String.format(Locale.ENGLISH, "%s%d", itemView.getContext().getString(R.string.order_text), orderResponse.getOrder().getId()));
            orderDateTextView.setText(orderResponse.getOrder().getStartDate());
            itemRecyclerView.setHasFixedSize(true);
            OrderItemAdapter adapter = new OrderItemAdapter();
            itemRecyclerView.setAdapter(adapter);
            adapter.setOrderItemList(itemList, orderResponse.getOrder());
            adapter.setItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onItemClicked(orderList.get(getAdapterPosition()));
        }

        @Override
        public void onItemClicked() {
            if (itemClickListener != null)
                itemClickListener.onItemClicked(orderList.get(getAdapterPosition()));
        }
    }

    public interface ItemClickListener {
        void onItemClicked(Object object);
    }

}
