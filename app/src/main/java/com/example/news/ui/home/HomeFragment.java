package com.example.news.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.ConfigurationCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.App;
import com.example.news.R;
import com.example.news.WebActivity;
import com.example.news.api.models.NewsDTO;
import com.example.news.api.models.Wrapper;
import com.example.news.databinding.FragmentHomeBinding;
import com.example.news.databinding.ItemNewsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dev.chrisbanes.insetter.Insetter;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    RecyclerView mRecyclerView;
    Adapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = binding.homeList;

        homeViewModel.getList().observe(this, new Observer<Wrapper<List<NewsDTO>>>() {
            @Override
            public void onChanged(Wrapper<List<NewsDTO>> listWrapper) {
                if (listWrapper.getError() != null) {
                    Log.e(getTag(), "onChanged: error", listWrapper.getError());
                    Toast.makeText(getContext(), R.string.error,Toast.LENGTH_SHORT).show();
                }else {
                    mAdapter.setList(listWrapper.getData());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        setRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setRecyclerView() {
        if (mAdapter == null) {
            mAdapter = new Adapter(getContext());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else mAdapter.notifyDataSetChanged();
    }

    public static class Adapter extends RecyclerView.Adapter<NewsHolder> {
        private List<NewsDTO> mList = new ArrayList<>();
        private Context mContext;

        public Adapter(Context context) {
            mContext = context;
        }

        public void setList(List<NewsDTO> list) {
            mList.clear();
            mList.addAll(list);
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