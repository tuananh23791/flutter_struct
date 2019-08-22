package com.beyondedge.hm.ui.screen;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.beyondedge.hm.BR;
import com.beyondedge.hm.R;
import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.config.HMConfig.Menu;
import com.beyondedge.hm.config.LoadConfig;
import com.bumptech.glide.Glide;

import timber.log.Timber;

/**
 * Created by Hoa Nguyen on Apr 23 2019.
 */
public class FollowUsMenuAdapter extends ListAdapter<Menu, FollowUsMenuAdapter.MenuViewHolder> {
    private static final DiffUtil.ItemCallback<Menu> DIFF_CALLBACK = new DiffUtil.ItemCallback<Menu>() {
        @Override
        public boolean areItemsTheSame(@NonNull Menu oldItem, @NonNull Menu newItem) {
            return oldItem.getName() == newItem.getName();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Menu oldItem, @NonNull Menu newItem) {
            return oldItem.equals(newItem);
        }
    };
    private OnItemClickListener mOnItemClickListener;

    public FollowUsMenuAdapter() {
        super(DIFF_CALLBACK);
    }

    @BindingAdapter("imageNetworkUrl")
    public static void loadImage(ImageView imageView, String url) {
        HMConfig config = LoadConfig.getInstance(imageView.getContext()).load();
        String fullPath = "";
        if (config != null) {
            try {
                fullPath = config.getVersion().getIconUrl() + url;
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        Timber.d("URL FollowUS: " + fullPath);
        Glide
                .with(imageView.getContext())
                .load(fullPath)
                .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
                .into(imageView);
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.follow_us_menu_item, parent, false);
        return new MenuViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu Menu = getItem(position);
        holder.bind(Menu);
    }

    public Menu getMenuAt(int pos) {
        return getItem(pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Menu Menu);
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                if (mOnItemClickListener != null && pos != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(getItem(pos));
                }
            }
        };

        public MenuViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Menu item) {
            binding.setVariable(BR.menu, item);
            binding.setVariable(BR.itemClickListener, mOnClickListener);
            binding.executePendingBindings();
        }
    }
}