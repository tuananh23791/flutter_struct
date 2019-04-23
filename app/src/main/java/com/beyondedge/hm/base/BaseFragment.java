package com.beyondedge.hm.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.beyondedge.hm.R;

/**
 * Created by Hoa Nguyen on Apr 23 2019.
 */
public class BaseFragment extends Fragment {
    public static BaseFragment newInstance() {

        Bundle args = new Bundle();

        BaseFragment fragment = new BaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.web_page_layout, container, false);
    }
}
