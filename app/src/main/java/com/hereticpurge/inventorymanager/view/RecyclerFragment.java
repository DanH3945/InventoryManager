package com.hereticpurge.inventorymanager.view;

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

    private RecyclerCallback mRecyclerCallback;

    public static RecyclerFragment createFragment(RecyclerCallback recyclerCallback){
        RecyclerFragment recyclerFragment = new RecyclerFragment();
        recyclerFragment.setRecyclerCallback(recyclerCallback);
        return recyclerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_fragment_layout, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerFragmentAdapter adapter = new RecyclerFragmentAdapter(this, mRecyclerCallback);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void setRecyclerCallback(RecyclerCallback recyclerCallback){
        mRecyclerCallback = recyclerCallback;
    }

    public interface RecyclerCallback {
        void onItemSelected(int position);
    }

}
