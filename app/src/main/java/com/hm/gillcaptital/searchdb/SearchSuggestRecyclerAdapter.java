package com.hm.gillcaptital.searchdb;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hm.gillcaptital.databinding.RecyclerSearchItemBinding;
import com.hm.gillcaptital.searchdb.server.SearchEntity;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggestRecyclerAdapter extends RecyclerView.Adapter<SearchSuggestRecyclerAdapter.SearchViewHolder> {
    private List<SearchEntity> itemList = new ArrayList<>();
    private SearchRecyclerInterface listener;

    public SearchSuggestRecyclerAdapter(SearchRecyclerInterface listener) {
        this.listener = listener;
    }

    public void setItems(final List<SearchEntity> items) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return itemList.size();
            }

            @Override
            public int getNewListSize() {
                return items.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                SearchEntity newItem = items.get(newItemPosition);
                SearchEntity oldItem = itemList.get(oldItemPosition);
                return oldItem.getText().equals(newItem.getText());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                SearchEntity oldItem = itemList.get(oldItemPosition);
                SearchEntity newItem = items.get(newItemPosition);
                return oldItem.getText().equals(newItem.getText());
            }
        });
        this.itemList.clear();
        this.itemList.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchViewHolder(RecyclerSearchItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.bindView(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface SearchRecyclerInterface {
        void onSearchItemClicked(String query);

        void onSearchDeleteClicked(SearchEntity searchEntity);
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        private RecyclerSearchItemBinding binding;

        SearchViewHolder(RecyclerSearchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView(final SearchEntity entity) {
            binding.setText(entity.getText());
            binding.itemHolder.setOnClickListener(view -> listener.onSearchItemClicked(entity.getText()));
            binding.imgDelete.setOnClickListener(view -> listener.onSearchDeleteClicked(entity));
        }

    }
}
