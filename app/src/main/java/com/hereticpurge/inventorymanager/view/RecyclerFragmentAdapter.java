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

import com.hereticpurge.inventorymanager.model.ProductItem;
import com.hereticpurge.inventorymanager.model.ProductViewModel;

import java.util.List;

public class RecyclerFragmentAdapter extends RecyclerView.Adapter<RecyclerFragmentAdapter.ViewHolder> {

    private List<ProductItem> productItems;
    private RecyclerCallback mCallback;

    RecyclerFragmentAdapter(RecyclerCallback callback) {
        this.mCallback = callback;
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
                mCallback.onItemSelected(productItems.get(i).id);
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

    public interface RecyclerCallback {
        void onItemSelected(int id);
    }
}
