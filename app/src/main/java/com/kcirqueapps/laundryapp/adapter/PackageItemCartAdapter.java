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

import de.hdodenhof.circleimageview.CircleImageView;

public class PackageItemCartAdapter extends RecyclerView.Adapter<PackageItemCartAdapter.PackageItemCartHolder> {
    private List<ServicePrice> servicePriceList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public PackageItemCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PackageItemCartHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_package_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PackageItemCartHolder holder, int position) {
        ServicePrice servicePrice = servicePriceList.get(position);
        holder.bindTo(servicePrice);
    }

    @Override
    public int getItemCount() {
        return servicePriceList.size();
    }

    public void setServicePriceList(List<ServicePrice> servicePriceList) {
        this.servicePriceList = servicePriceList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class PackageItemCartHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView serviceImageView;
        private TextView serviceNameTextView, quantityTextView;
        private ImageView deleteImageView;

        PackageItemCartHolder(@NonNull View itemView) {
            super(itemView);
            serviceImageView = itemView.findViewById(R.id.service_image_view);
            serviceNameTextView = itemView.findViewById(R.id.service_name_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            deleteImageView = itemView.findViewById(R.id.delete_image_view);
            deleteImageView.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        void bindTo(ServicePrice servicePrice) {
            serviceNameTextView.setText(servicePrice.getCloth());
            quantityTextView.setText(String.format("%s %s", servicePrice.getUnit(), itemView.getContext().getString(R.string.unit_text)));
            Glide.with(itemView).load(servicePrice.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(serviceImageView);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.delete_image_view) {
                if (onItemClickListener != null)
                    onItemClickListener.onRemoveClicked(servicePriceList.get(getAdapterPosition()));
            } else {

                if (onItemClickListener != null)
                    onItemClickListener.onItemClicked(servicePriceList.get(getAdapterPosition()));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(ServicePrice servicePrice);

        void onRemoveClicked(ServicePrice servicePrice);
    }
}
