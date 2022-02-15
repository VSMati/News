package com.example.news.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.R;
import com.example.news.WebActivity;
import com.example.news.api.models.NewsDTO;
import com.example.news.databinding.FragmentHomeBinding;
import com.example.news.databinding.ItemNewsBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private HomeViewModel mViewModel;
    private FragmentHomeBinding mBinding;
    private SwipeRefreshLayout mSwipe;

    RecyclerView mRecyclerView;
    Adapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mSwipe = mBinding.swipeContainer;

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = mBinding.homeList;

        mSwipe.setOnRefreshListener(this::refresh);
        mSwipe.setColorSchemeColors(mBinding.getRoot().getResources().getColor(R.color.purple_700),
                mBinding.getRoot().getResources().getColor(R.color.purple_200));

        setRecyclerView();
        refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecyclerView.setAdapter(null);
        mAdapter = null;
        mRecyclerView = null;
        mBinding = null;
    }

    private void setRecyclerView() {
        if (mAdapter == null) {
            mAdapter = new Adapter(getContext());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else mAdapter.notifyDataSetChanged();
    }

    private void refresh() {
        mViewModel.getList().observe(this, listWrapper -> {
            if (listWrapper.getError() != null) {
                Toast.makeText(getContext(), R.string.error,Toast.LENGTH_SHORT).show();
            }else {
                mAdapter.setList(listWrapper.getData());
            }
            mSwipe.setRefreshing(false);
        });
    }

    public static class Adapter extends RecyclerView.Adapter<NewsHolder> {
        private final List<NewsDTO> mList = new ArrayList<>();
        private final Context mContext;

        public Adapter(Context context) {
            mContext = context;
        }

        public void setList(List<NewsDTO> list) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }

        @NonNull
        @NotNull
        @Override
        public NewsHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater =
                    LayoutInflater.from(parent.getContext());
            ItemNewsBinding itemBinding =
                    ItemNewsBinding.inflate(layoutInflater, parent, false);
            NewsHolder holder = new NewsHolder(itemBinding, new ClickListener() {
                @Override
                public void onClick(int position) {
                    String url = mList.get(position).getUrl();
                    Intent intent = WebActivity.getIntent(mContext, url);
                    mContext.startActivity(intent);
                }

                @Override
                public void onShare(int position) {
                    String url = mList.get(position).getUrl();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    mContext.startActivity(shareIntent);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull NewsHolder holder, int position) {
            holder.bind(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

    }

    public static class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemNewsBinding binding;
        ClickListener mClickListener;

        public NewsHolder(@NonNull @NotNull ItemNewsBinding binding, ClickListener click) {
            super(binding.getRoot());
            this.binding = binding;
            this.mClickListener = click;

            binding.getRoot().setOnClickListener(this);
            binding.share.setOnClickListener(this);
        }

        public void bind(NewsDTO news) {
            binding.heading.setText(news.getTitle());
            binding.text.setText(news.getContent());
            binding.publisher.setText(news.getAuthor());
            Picasso.get().load(news.getUrlToImage()).fit().centerCrop()
                    .placeholder(R.drawable.news_placeholder)
                    .into(binding.image);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == binding.getRoot().getId()) {
                mClickListener.onClick(this.getLayoutPosition());
            }
            if (v.getId() == binding.share.getId()) {
                mClickListener.onShare(this.getLayoutPosition());
            }
        }
    }

    public interface ClickListener {
        void onClick(int position);
        void onShare(int position);
    }
}