package com.hereticpurge.inventorymanager.view;

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

public class RecyclerFragment extends Fragment {

    private RecyclerFragmentAdapter mAdapter;

    private RecyclerCallback mRecyclerCallback;

    public static RecyclerFragment createFragment(RecyclerCallback recyclerCallback){
        RecyclerFragment recyclerFragment = new RecyclerFragment();
        recyclerFragment.setRecyclerCallback(recyclerCallback);
        return recyclerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_fragment_layout, container);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new RecyclerFragmentAdapter(this, mRecyclerCallback);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void setRecyclerCallback(RecyclerCallback recyclerCallback){
        mRecyclerCallback = recyclerCallback;
    }

    public interface RecyclerCallback {
        void onItemSelected(int position);
    }

}
