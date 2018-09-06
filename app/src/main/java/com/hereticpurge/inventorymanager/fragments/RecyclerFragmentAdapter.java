package com.hereticpurge.inventorymanager.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductItem;
import com.hereticpurge.inventorymanager.model.ProductViewModel;
import com.hereticpurge.inventorymanager.utils.CurrencyUtils;
import com.hereticpurge.inventorymanager.utils.CustomImageUtils;

import java.util.List;

public class RecyclerFragmentAdapter extends RecyclerView.Adapter<RecyclerFragmentAdapter.ViewHolder> {

    private List<ProductItem> mProductItemList;
    private RecyclerCallback mCallback;
    private ProductViewModel mViewmodel;

    RecyclerFragmentAdapter(RecyclerCallback callback, ProductViewModel viewModel) {
        this.mCallback = callback;
        this.mViewmodel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_fragment_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        ProductItem productItem = mProductItemList.get(i);

        CustomImageUtils.loadImage(viewHolder.itemView.getContext(),
                productItem.getName(),
                viewHolder.mImageView);

        viewHolder.mNameText.setText(productItem.getName());
        viewHolder.mCost.setText(CurrencyUtils.addLocalCurrencySymbol(productItem.getCost()));
        viewHolder.mRetail.setText(CurrencyUtils.addLocalCurrencySymbol(productItem.getRetail()));
        viewHolder.mCurrentStock.setText(String.valueOf(productItem.getCurrentStock()));

        viewHolder.mTrackingSwitch.setChecked(productItem.isTracked());

        viewHolder.mTrackingSwitch.setOnClickListener(v -> {
            productItem.setTracked(!productItem.isTracked());
            mViewmodel.addProduct(productItem);
        });

        viewHolder.itemView.setOnClickListener(view -> mCallback.onItemSelected(i));
    }

    @Override
    public int getItemCount() {
        return mProductItemList == null ? 0 : mProductItemList.size();
    }

    public void updateList(List<ProductItem> productItems) {
        this.mProductItemList = productItems;
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        TextView mNameText;
        TextView mCost;
        TextView mRetail;
        TextView mCurrentStock;

        Switch mTrackingSwitch;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.recycler_item_image_view);

            mNameText = itemView.findViewById(R.id.recycler_item_product_name);
            mCost = itemView.findViewById(R.id.recycler_item_cost);
            mRetail = itemView.findViewById(R.id.recycler_item_retail);
            mCurrentStock = itemView.findViewById(R.id.recycler_item_current_stock);

            mTrackingSwitch = itemView.findViewById(R.id.recycler_item_tracking_switch);
        }
    }

    public interface RecyclerCallback {
        void onItemSelected(int id);
    }
}
