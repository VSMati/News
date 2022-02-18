package com.example.news.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.databinding.FragmentSearchBinding;
import com.example.news.ui.ListNews;

import org.jetbrains.annotations.NotNull;

public class SearchFragment extends Fragment {
    private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;

    private RecyclerView mRecyclerView;
    private ListNews.Adapter mAdapter;
    private SearchView mSearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        mRecyclerView = binding.searchList;
        mSearch = binding.search;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupList();
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRecyclerView.setVisibility(View.VISIBLE);
                fetchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mRecyclerView.setVisibility(View.INVISIBLE);
                return false;
            }
        });
    }

    private void setupList(){
        if (mAdapter == null) {
            mAdapter = new ListNews.Adapter(getContext());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void fetchData(String q) {
        searchViewModel.setQuery(q);
        searchViewModel.getList().observe(this, listWrapper -> {
            if (listWrapper.getError() != null) {
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }else {
                mAdapter.setList(listWrapper.getData());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecyclerView.setAdapter(null);
        mAdapter = null;
        mRecyclerView = null;
        binding = null;
    }
}