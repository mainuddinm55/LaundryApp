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
import com.kcirqueapps.laundryapp.database.model.Cart;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    private List<Cart> cartList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.bindTo(cart);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class CartHolder extends RecyclerView.ViewHolder {
        private final CircleImageView serviceImageView;
        private final TextView nameTextView, serviceTextView, quantityTextView, amountTextView;
        private final ImageView deleteImageView;

        CartHolder(@NonNull View itemView) {
            super(itemView);
            serviceImageView = itemView.findViewById(R.id.service_image_view);
            nameTextView = itemView.findViewById(R.id.service_name_text_view);
            serviceTextView = itemView.findViewById(R.id.service_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            amountTextView = itemView.findViewById(R.id.price_text_view);
            deleteImageView = itemView.findViewById(R.id.delete_image_view);
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClicked(cartList.get(getAdapterPosition()));
                }
            });
        }

        void bindTo(Cart cart) {
            Glide.with(itemView).load(cart.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(serviceImageView);
            nameTextView.setText(cart.getServiceName());
            serviceTextView.setText(cart.getServiceType());
            quantityTextView.setText(String.format("%s %s", cart.getUnit(), itemView.getContext().getString(R.string.unit_text)));
            amountTextView.setText(String.format("%s %s", itemView.getContext().getString(R.string.tk_sign), cart.getAmount()));
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Cart cart);
    }
}
