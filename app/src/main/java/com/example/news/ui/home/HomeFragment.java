package com.example.news.ui.home;

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
import com.example.news.databinding.FragmentHomeBinding;
import com.example.news.databinding.ItemCategoryBinding;
import com.example.news.ui.ListNews;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private HomeViewModel mViewModel;
    private FragmentHomeBinding mBinding;
    private SwipeRefreshLayout mSwipe;

    RecyclerView mNewsList;
    ListNews.Adapter mNewsAdapter;

    RecyclerView mCategoryList;
    Adapter mCategoryAdapter;

    private String category;

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
        mNewsList = mBinding.homeList;
        mCategoryList = mBinding.categoryList;
        mCategoryList.setNestedScrollingEnabled(false);


        List<String> categories = Arrays.asList(getResources().getStringArray(R.array.categories));
        mCategoryAdapter = new Adapter(data -> {
            category = data;
            refresh();
            mNewsAdapter.notifyDataSetChanged();
        },categories);


        mSwipe.setOnRefreshListener(this::refresh);
        mSwipe.setColorSchemeColors(mBinding.getRoot().getResources().getColor(R.color.purple_700),
                mBinding.getRoot().getResources().getColor(R.color.purple_200));

        setRecyclerView();
        refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNewsList.setAdapter(null);
        mNewsAdapter = null;
        mNewsList = null;
        mBinding = null;
    }

    private void setRecyclerView() {
        if (mNewsAdapter == null) {
            mNewsAdapter = new ListNews.Adapter(getContext());
            mNewsList.setAdapter(mNewsAdapter);
            mNewsList.setLayoutManager(new LinearLayoutManager(getContext()));
        } else mNewsAdapter.notifyDataSetChanged();

        mCategoryList.setAdapter(mCategoryAdapter);
        mCategoryList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
    }

    private void refresh() {
        if (category == null) {
            mViewModel.getList().observe(this, listWrapper -> {
                if (listWrapper.getError() != null) {
                    Toast.makeText(getContext(), R.string.error,Toast.LENGTH_SHORT).show();
                }else {
                    mNewsAdapter.setList(listWrapper.getData());
                }
                mSwipe.setRefreshing(false);
            });
        } else {
            mViewModel.getList(category).observe(this, listWrapper -> {
                if (listWrapper.getError() != null) {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }else {
                    mNewsAdapter.setList(listWrapper.getData());
                }
                mSwipe.setRefreshing(false);
            });
        }
    }
/**Adapter for list that shows categories*/
    public static class Adapter extends RecyclerView.Adapter<CategoryHolder> {
        private static final List<String> LIST = (Arrays.asList("business", "entertainment",
                "general", "health", "science", "sport", "technology"));
        private final List<String> listToShow;
        private String category;
        private final DataTransfer mDataTransfer;

        public Adapter(DataTransfer dataTransfer, List<String> showVal) {
            mDataTransfer = dataTransfer;
            listToShow = showVal;
        }

        @NonNull
        @NotNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater =
                    LayoutInflater.from(parent.getContext());
            ItemCategoryBinding binding =
                    ItemCategoryBinding.inflate(layoutInflater, parent, false);
            CategoryHolder holder = new CategoryHolder(binding, position -> {
                category = LIST.get(position);
                mDataTransfer.transferData(category);
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull CategoryHolder holder, int position) {
            holder.bind(listToShow.get(position));
        }

        @Override
        public int getItemCount() {
            return listToShow.size();
        }


    }
    public static class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CategoryClick mClick;
        ItemCategoryBinding mBinding;

        public CategoryHolder(ItemCategoryBinding binding, CategoryClick click) {
            super(binding.getRoot());
            mBinding = binding;
            mClick = click;

            mBinding.getRoot().setOnClickListener(this);
        }

        public void bind(String category) {
            mBinding.setCategory(category);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            mClick.onClick(this.getLayoutPosition());
        }
    }
/**Get chosen category on click*/
    public interface CategoryClick {
        void onClick(int position);
    }
/**Transfer chosen category from Adapter to Fragment*/
    public interface DataTransfer {
        void transferData(String data);
    }
}