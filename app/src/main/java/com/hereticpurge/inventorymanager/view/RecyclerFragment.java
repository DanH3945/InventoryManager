package com.hereticpurge.inventorymanager.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.RecyclerCallback;
import com.hereticpurge.inventorymanager.database.ProductItem;
import com.hereticpurge.inventorymanager.database.ProductViewModel;

import java.util.List;

public class RecyclerFragment extends Fragment {

    private RecyclerFragmentAdapter mAdapter;

    private RecyclerCallback mRecyclerCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_fragment_layout, container);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new RecyclerFragmentAdapter(mRecyclerCallback);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ViewModelProviders.of(this)
                .get(ProductViewModel.class)
                .getProductList()
                .observe(this, new Observer<List<ProductItem>>() {
                    @Override
                    public void onChanged(@Nullable List<ProductItem> productItems) {
                        setProductItems(productItems);
                    }
                });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void setProductItems(List<ProductItem> productItems) {
        mAdapter.updateList(productItems);
    }

    public void setRecyclerCallback(RecyclerCallback recyclerCallback) {
        this.mRecyclerCallback = recyclerCallback;
    }

}
