package com.hm.gillcaptital.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hm.gillcaptital.R;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public class EmptyFragment extends BaseFragment {
    public static final String EXTRA_TEXT_INFO = "EXTRA_TEXT_INFO";
    private String mTextInfo = "";

    public static EmptyFragment newInstance(String textInfo) {

        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT_INFO, textInfo);
        EmptyFragment fragment = new EmptyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static EmptyFragment newInstance() {

        Bundle args = new Bundle();

        EmptyFragment fragment = new EmptyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTextInfo = getArguments().getString(EXTRA_TEXT_INFO);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.empty_text_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textInfo = view.findViewById(R.id.textInfo);

        textInfo.setText(mTextInfo);
        textInfo.setVisibility(TextUtils.isEmpty(mTextInfo) ? View.GONE : View.VISIBLE);

    }
}
