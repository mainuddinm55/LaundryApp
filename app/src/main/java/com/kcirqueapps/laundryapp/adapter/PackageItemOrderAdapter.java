package com.kcirqueapps.laundryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.network.Model.ServicePrice;
import com.kcirqueapps.laundryapp.utils.BanglaUtils;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PackageItemOrderAdapter extends RecyclerView.Adapter<PackageItemOrderAdapter.PackageItemOrderHolder> {
    private int maxItem = 0;
    private List<ServicePrice> servicePriceList = new ArrayList<>();
    private OnItemClickedListener onItemClickedListener;

    public PackageItemOrderAdapter(int maxItem) {
        this.maxItem = maxItem;
    }

    @NonNull
    @Override
    public PackageItemOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PackageItemOrderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_package_item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PackageItemOrderHolder holder, int position) {
        ServicePrice servicePrice = servicePriceList.get(position);
        holder.bindTo(servicePrice);
    }

    @Override
    public int getItemCount() {
        return servicePriceList.size();
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void setServicePriceList(List<ServicePrice> servicePriceList) {
        this.servicePriceList = servicePriceList;
        notifyDataSetChanged();
    }

    class PackageItemOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CustomCheckBox.OnCheckedChangeListener {
        private CircleImageView serviceImageView;
        private TextView serviceNameTextView, quantityTextView;
        private Button quantityIncrementBtn, quantityDecrementBtn;
        private CustomCheckBox checkBox;

        PackageItemOrderHolder(@NonNull View itemView) {
            super(itemView);
            serviceImageView = itemView.findViewById(R.id.service_image_view);
            serviceNameTextView = itemView.findViewById(R.id.service_name_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            quantityIncrementBtn = itemView.findViewById(R.id.quantity_increment_btn);
            quantityDecrementBtn = itemView.findViewById(R.id.quantity_decrement_btn);
            checkBox = itemView.findViewById(R.id.check_box);

            quantityDecrementBtn.setOnClickListener(this);
            quantityIncrementBtn.setOnClickListener(this);
            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(this);
        }

        void bindTo(ServicePrice servicePrice) {
            Glide.with(itemView).load(servicePrice.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(serviceImageView);
            serviceNameTextView.setText(servicePrice.getCloth());

        }

        @Override
        public void onClick(View v) {
            int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
            switch (v.getId()) {
                case R.id.quantity_decrement_btn:
                    if (currentQuantity > 1) {
                        currentQuantity--;
                        quantityTextView.setText(String.valueOf(currentQuantity));
                    }
                    break;
                case R.id.quantity_increment_btn:
                    if (currentQuantity < maxItem) {
                        currentQuantity++;
                        quantityTextView.setText(String.valueOf(currentQuantity));
                    }
                    break;
                default:
                    checkBox.setChecked(!checkBox.isChecked(), true);
                    onItemClickedListener.onItemClicked(servicePriceList.get(getAdapterPosition()));
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
            int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
            if (isChecked) {
                if (maxItem >= currentQuantity)
                    maxItem = maxItem - currentQuantity;
            } else {
                maxItem = maxItem + currentQuantity;
            }
            if (onItemClickedListener != null) {
                ServicePrice servicePrice = servicePriceList.get(getAdapterPosition());
                servicePrice.setUnit(BanglaUtils.toString(Integer.parseInt(quantityTextView.getText().toString())));
                onItemClickedListener.onCheckedChanged(servicePrice, checkBox, isChecked);
            }
        }
    }

    public interface OnItemClickedListener {
        void onItemClicked(ServicePrice servicePrice);

        void onCheckedChanged(ServicePrice servicePrice, CustomCheckBox customCheckBox, boolean isChecked);
    }
}
