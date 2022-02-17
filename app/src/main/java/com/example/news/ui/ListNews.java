package com.example.news.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.WebActivity;
import com.example.news.api.models.NewsDTO;
import com.example.news.databinding.ItemNewsBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
/**Unified list of news for all fragments*/
public class ListNews {
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
    /**WebView and ShareSheet on click*/
    public interface ClickListener {
        void onClick(int position);
        void onShare(int position);
    }
}
