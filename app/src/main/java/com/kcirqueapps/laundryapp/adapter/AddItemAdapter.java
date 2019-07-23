package com.kcirqueapps.laundryapp.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.database.model.Cart;
import com.kcirqueapps.laundryapp.network.Model.ServicePrice;
import com.kcirqueapps.laundryapp.utils.BanglaUtils;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddItemAdapter extends RecyclerView.Adapter<AddItemAdapter.AddItemHolder> {
    private List<ServicePrice> servicePrices = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public AddItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_add_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddItemHolder holder, int position) {
        ServicePrice servicePrice = servicePrices.get(position);
        holder.bindTo(servicePrice);
    }

    @Override
    public int getItemCount() {
        return servicePrices.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setServicePrices(List<ServicePrice> servicePrices) {
        this.servicePrices = servicePrices;
        notifyDataSetChanged();
    }

    class AddItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        private CircleImageView serviceImageView;
        private TextView serviceNameTextView, quantityTextView;
        private Button incrementQuantityBtn, decrementQuantityBtn;
        private CheckBox ironCheckBox, washCheckBox;
        private CustomCheckBox selectCheckBox;

        private int quantity = 1;
        private List<String> services = new ArrayList<>();
        private double unitAmount = 0;
        private double totalAmount = 0;

        AddItemHolder(@NonNull final View itemView) {
            super(itemView);
            serviceImageView = itemView.findViewById(R.id.service_image_view);
            serviceNameTextView = itemView.findViewById(R.id.service_name_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            incrementQuantityBtn = itemView.findViewById(R.id.quantity_increment_btn);
            decrementQuantityBtn = itemView.findViewById(R.id.quantity_decrement_btn);
            ironCheckBox = itemView.findViewById(R.id.iron_check_box);
            washCheckBox = itemView.findViewById(R.id.wash_check_box);
            selectCheckBox = itemView.findViewById(R.id.check_box);
        }

        void bindTo(ServicePrice servicePrice) {
            quantity = 1;
            services = new ArrayList<>();
            unitAmount = 0;
            totalAmount = 0;
            Glide.with(itemView).load(servicePrice.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(serviceImageView);
            serviceNameTextView.setText(servicePrice.getCloth());

            incrementQuantityBtn.setOnClickListener(this);
            decrementQuantityBtn.setOnClickListener(this);
            itemView.setOnClickListener(this);

            ironCheckBox.setOnCheckedChangeListener(this);
            washCheckBox.setOnCheckedChangeListener(this);

            selectCheckBox.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                    if (onItemClickListener != null) {
                        String service = TextUtils.join(", ", services);
                        if (service == null || service.isEmpty()) {
                            onItemClickListener.onError("আপনার সার্ভিসটি নির্বাচন করুন");
                        } else {
                            if (isChecked) {
                                ironCheckBox.setEnabled(false);
                                washCheckBox.setEnabled(false);
                                incrementQuantityBtn.setEnabled(false);
                                decrementQuantityBtn.setEnabled(false);
                            }else {
                                ironCheckBox.setEnabled(true);
                                washCheckBox.setEnabled(true);
                                incrementQuantityBtn.setEnabled(true);
                                decrementQuantityBtn.setEnabled(true);
                            }
                            ServicePrice servicePrice = servicePrices.get(getAdapterPosition());
                            Cart cart = new Cart(servicePrice.getId(), servicePrice.getCloth(), service, BanglaUtils.toString(quantity),
                                    BanglaUtils.toString(unitAmount), BanglaUtils.toString(totalAmount), servicePrice.getImageUrl(), false);
                            onItemClickListener.onCheckedChanged(servicePrice, cart, checkBox, isChecked, getAdapterPosition());
                        }

                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.quantity_increment_btn:
                    quantity++;
                    quantityTextView.setText(String.valueOf(quantity));
                    totalAmount = unitAmount * quantity;
                    selectCheckBox.setChecked(selectCheckBox.isChecked());
                    break;
                case R.id.quantity_decrement_btn:
                    if (quantity != 0) {
                        quantity--;
                        totalAmount = unitAmount * quantity;
                        quantityTextView.setText(String.valueOf(quantity));
                        selectCheckBox.setChecked(selectCheckBox.isChecked());
                    }
                    break;
                default:
                    assert onItemClickListener != null;
                    String service = TextUtils.join(", ", services);
                    if (service == null || service.isEmpty()) {
                        onItemClickListener.onError("আপনার সার্ভিসটি নির্বাচন করুন");
                        if (selectCheckBox.isChecked()) {
                            selectCheckBox.setChecked(false, true);
                        }
                    } else {
                        selectCheckBox.setChecked(!selectCheckBox.isChecked(), true);
                        ServicePrice servicePrice = servicePrices.get(getAdapterPosition());
                        Cart cart = new Cart(servicePrice.getId(), servicePrice.getCloth(), service, BanglaUtils.toString(quantity),
                                BanglaUtils.toString(unitAmount), BanglaUtils.toString(totalAmount), servicePrice.getImageUrl(), false);
                        onItemClickListener.onItemClicked(servicePrice, cart);
                    }
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.iron_check_box:
                    double price = BanglaUtils.toDouble(servicePrices.get(getAdapterPosition()).getIronPrice());
                    if (isChecked) {
                        unitAmount = unitAmount + price;
                        totalAmount = unitAmount * quantity;
                        services.add(itemView.getContext().getString(R.string.iron_price_text));
                        if (selectCheckBox.isChecked()) {
                            selectCheckBox.setChecked(true, true);
                        }
                    } else {
                        unitAmount = unitAmount - price;
                        totalAmount = unitAmount * quantity;
                        services.remove(itemView.getContext().getString(R.string.iron_price_text));
                        if (selectCheckBox.isChecked()) {
                            selectCheckBox.setChecked(true, true);
                        }
                    }
                    break;
                case R.id.wash_check_box:
                    double washPrice = BanglaUtils.toDouble(servicePrices.get(getAdapterPosition()).getWashPrice());
                    if (isChecked) {
                        unitAmount = unitAmount + washPrice;
                        totalAmount = unitAmount * quantity;
                        services.add(itemView.getContext().getString(R.string.iron_price_text));
                        if (selectCheckBox.isChecked()) {
                            selectCheckBox.setChecked(true, true);
                        }

                    } else {
                        unitAmount = unitAmount - washPrice;
                        totalAmount = unitAmount * quantity;
                        services.remove(itemView.getContext().getString(R.string.iron_price_text));
                        if (selectCheckBox.isChecked()) {
                            selectCheckBox.setChecked(true, true);
                        }
                    }
                    break;
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(ServicePrice servicePackage, Cart cart);

        void onCheckedChanged(ServicePrice servicePackage, Cart cart, CustomCheckBox customCheckBox, boolean isChecked, int position);

        void onError(String message);
    }
}
