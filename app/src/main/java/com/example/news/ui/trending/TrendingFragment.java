package com.example.news.ui.trending;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.R;
import com.example.news.api.models.NewsDTO;
import com.example.news.api.models.Wrapper;
import com.example.news.databinding.FragmentTrendingBinding;
import com.example.news.ui.ListNews;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrendingFragment extends Fragment {
    private TrendingViewModel trendingViewModel;
    private FragmentTrendingBinding mBinding;

    private RecyclerView mRecyclerView;
    private ListNews.Adapter mAdapter;
    private SwipeRefreshLayout mSwipe;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        trendingViewModel =
                new ViewModelProvider(this).get(TrendingViewModel.class);

        mBinding = FragmentTrendingBinding.inflate(inflater, container, false);
        mRecyclerView = mBinding.trendList;
        mSwipe = mBinding.getRoot();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipe.setOnRefreshListener(this::refresh);
        mSwipe.setColorSchemeColors(mBinding.getRoot().getResources().getColor(R.color.purple_700),
                mBinding.getRoot().getResources().getColor(R.color.purple_200));
        setupList();
        refresh();

    }

    private void refresh() {
        trendingViewModel.getList().observe(this, new Observer<Wrapper<List<NewsDTO>>>() {
            @Override
            public void onChanged(Wrapper<List<NewsDTO>> listWrapper) {
                if (listWrapper.getError() != null) {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }else {
                    mAdapter.setList(listWrapper.getData());
                }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecyclerView.setAdapter(null);
        mAdapter = null;
        mRecyclerView = null;
        mBinding = null;
    }
}