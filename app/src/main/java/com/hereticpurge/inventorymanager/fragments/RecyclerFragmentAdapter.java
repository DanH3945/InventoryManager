package com.hereticpurge.inventorymanager.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductItem;

import java.util.List;

public class RecyclerFragmentAdapter extends RecyclerView.Adapter<RecyclerFragmentAdapter.ViewHolder> {

    private List<ProductItem> mProductItemList;
    private RecyclerCallback mCallback;

    RecyclerFragmentAdapter(RecyclerCallback callback) {
        this.mCallback = callback;
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

        viewHolder.productId.setText(Integer.toString(productItem.getId()));
        viewHolder.productName.setText(productItem.getName());
        viewHolder.barcode.setText(productItem.getBarcode());
        viewHolder.currentStock.setText(Integer.toString(productItem.getCurrentStock()));

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

        private TextView productId;
        private TextView productName;
        private TextView barcode;
        private TextView currentStock;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.productId = itemView.findViewById(R.id.rv_item_id);
            this.productName = itemView.findViewById(R.id.rv_item_name);
            this.barcode = itemView.findViewById(R.id.rv_item_barcode);
            this.currentStock = itemView.findViewById(R.id.rv_item_current_stock);
        }
    }

    public interface RecyclerCallback {
        void onItemSelected(int id);
    }
}
