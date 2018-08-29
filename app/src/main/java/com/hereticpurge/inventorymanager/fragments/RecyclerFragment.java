package com.hereticpurge.inventorymanager.fragments;

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
import com.hereticpurge.inventorymanager.model.ProductViewModel;

public class RecyclerFragment extends Fragment {

    private RecyclerFragmentAdapter mAdapter;

    private ProductViewModel mViewModel;

    public static RecyclerFragment createInstance(RecyclerFragmentAdapter.RecyclerCallback recyclerCallback){
        RecyclerFragment recyclerFragment = new RecyclerFragment();
        recyclerFragment.mAdapter = new RecyclerFragmentAdapter(recyclerCallback);
        return recyclerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_fragment_layout, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mViewModel.getProductList()
                .observe(this, productItems -> mAdapter.updateList(productItems));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mViewModel = ViewModelProviders.of(this)
                .get(ProductViewModel.class);

    }
}
