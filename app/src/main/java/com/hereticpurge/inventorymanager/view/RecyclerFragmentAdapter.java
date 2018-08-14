package com.hereticpurge.inventorymanager.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hereticpurge.inventorymanager.database.ProductItem;
import com.hereticpurge.inventorymanager.database.ProductViewModel;

import java.util.List;

public class RecyclerFragmentAdapter extends RecyclerView.Adapter<RecyclerFragmentAdapter.ViewHolder> {

    private List<ProductItem> productItems;
    private RecyclerFragment.RecyclerCallback mCallback;

    RecyclerFragmentAdapter(Fragment fragment, RecyclerFragment.RecyclerCallback callback) {
        this.mCallback = callback;

        ViewModelProviders.of(fragment)
                .get(ProductViewModel.class)
                .getProductList()
                .observe(fragment, new Observer<List<ProductItem>>() {
                    @Override
                    public void onChanged(@Nullable List<ProductItem> productItems) {
                        updateList(productItems);
                    }
                });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onItemSelected(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productItems == null ? 0 : productItems.size();
    }

    public void updateList(List<ProductItem> productItems) {
        this.productItems = productItems;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView barcode;
        TextView currentStock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
