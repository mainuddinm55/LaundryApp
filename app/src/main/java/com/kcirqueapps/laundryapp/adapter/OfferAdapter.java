package com.kcirqueapps.laundryapp.adapter;

import android.graphics.Paint;
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
import com.kcirqueapps.laundryapp.network.Model.OfferItem;

import java.util.ArrayList;
import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferHolder> {
    private List<OfferItem> offerItemList = new ArrayList<>();
    private int totalItem = 0;
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public OfferHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OfferHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OfferHolder holder, int position) {
        OfferItem item = offerItemList.get(position);
        holder.bindTo(item);
    }

    @Override
    public int getItemCount() {
        return totalItem;
    }

    public void setOfferItemList(List<OfferItem> offerItemList, int totalItem) {
        this.offerItemList = offerItemList;
        this.totalItem = totalItem;
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class OfferHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView serviceImageView;
        private TextView serviceNameTextView, discountPercentTextView, regularPriceTextView, priceTextView;

        OfferHolder(@NonNull View itemView) {
            super(itemView);
            serviceImageView = itemView.findViewById(R.id.service_image_view);
            serviceNameTextView = itemView.findViewById(R.id.service_name_text_view);
            discountPercentTextView = itemView.findViewById(R.id.discount_text_view);
            regularPriceTextView = itemView.findViewById(R.id.regular_price_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
            itemView.setOnClickListener(this);
        }

        void bindTo(OfferItem item) {
            Glide.with(itemView).load(item.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(serviceImageView);
            serviceNameTextView.setText(item.getCloth());
            discountPercentTextView.setText(String.format("%s%s", item.getDiscountPercent(), itemView.getContext().getString(R.string.discount_text)));
            regularPriceTextView.setText(String.format("%s %s", item.getRegularPrice(), itemView.getContext().getString(R.string.tk_sign)));
            priceTextView.setText(String.format("%s %s", item.getActualPrice(), itemView.getContext().getString(R.string.tk_sign)));

        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClicked(offerItemList.get(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(OfferItem offerItem);
    }
}
