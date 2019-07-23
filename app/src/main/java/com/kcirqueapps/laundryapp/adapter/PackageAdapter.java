package com.kcirqueapps.laundryapp.adapter;

import android.media.Image;
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
import com.kcirqueapps.laundryapp.network.Model.ServicePackage;

import java.util.ArrayList;
import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageHolder> {
    private List<ServicePackage> servicePackageList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public PackageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PackageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_package, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PackageHolder holder, int position) {
        ServicePackage servicePackage = servicePackageList.get(position);
        holder.bindTo(servicePackage);
    }

    @Override
    public int getItemCount() {
        return servicePackageList.size();
    }

    public void setServicePackageList(List<ServicePackage> servicePackageList) {
        this.servicePackageList = servicePackageList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class PackageHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTextView, serviceNameTextView, durationTextView, unitTextView, priceTextView;

        PackageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            nameTextView = itemView.findViewById(R.id.package_name_text_view);
            serviceNameTextView = itemView.findViewById(R.id.service_name_text_view);
            durationTextView = itemView.findViewById(R.id.duration_text_view);
            unitTextView = itemView.findViewById(R.id.uni_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClicked(servicePackageList.get(getAdapterPosition()));
                }
            });
        }

        void bindTo(ServicePackage servicePackage) {
            Glide.with(itemView).load(servicePackage.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView);
            nameTextView.setText(servicePackage.getName());
            serviceNameTextView.setText(servicePackage.getServiceType());
            durationTextView.setText(String.format("%s %s", itemView.getContext().getResources().getString(R.string.duration_text), servicePackage.getDuration()));
            unitTextView.setText(String.format("%s %s", servicePackage.getUnit(), imageView.getContext().getResources().getString(R.string.unit_text)));
            priceTextView.setText(String.format("%s %s %s", imageView.getContext().getResources().getString(R.string.price_text), servicePackage.getAmount(), itemView.getContext().getResources().getString(R.string.tk_sign)));
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(ServicePackage servicePackage);
    }
}
