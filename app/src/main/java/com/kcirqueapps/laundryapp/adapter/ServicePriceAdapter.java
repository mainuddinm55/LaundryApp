package com.kcirqueapps.laundryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.network.Model.ServicePrice;

import java.util.ArrayList;
import java.util.List;

public class ServicePriceAdapter extends RecyclerView.Adapter<ServicePriceAdapter.ServicePriceListHolder> {
    private List<ServicePrice> servicePriceList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ServicePriceListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServicePriceListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_price, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServicePriceListHolder holder, int position) {
        ServicePrice servicePrice = servicePriceList.get(position);
        holder.bindTo(servicePrice);
    }

    public void setServicePriceList(List<ServicePrice> servicePriceList) {
        this.servicePriceList = servicePriceList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return servicePriceList.size();
    }

    class ServicePriceListHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView, ironPriceTextView, washTextView;

        ServicePriceListHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            nameTextView = itemView.findViewById(R.id.service_name_text_view);
            ironPriceTextView = itemView.findViewById(R.id.iron_price_text_view);
            washTextView = itemView.findViewById(R.id.price_text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClicked(servicePriceList.get(getAdapterPosition()));
                }
            });
        }

        void bindTo(ServicePrice servicePrice) {
            Glide.with(itemView).load(servicePrice.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView);
            nameTextView.setText(servicePrice.getCloth());
            washTextView.setText(String.format("%s %s %s", itemView.getContext().getResources().getText(R.string.wash_price_text), servicePrice.getWashPrice(), itemView.getContext().getResources().getText(R.string.tk_sign)));
            ironPriceTextView.setText(String.format("%s %s %s", itemView.getContext().getResources().getText(R.string.iron_price_text), servicePrice.getIronPrice(), itemView.getContext().getResources().getText(R.string.tk_sign)));
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(ServicePrice servicePrice);
    }
}
