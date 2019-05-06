package com.beyondedge.hm.ui.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.beyondedge.hm.R;

/**
 * Created by Hoa Nguyen on May 06 2019.
 */
public class LoadingDialogFragment extends DialogFragment {


    public static LoadingDialogFragment newInstance() {

        Bundle args = new Bundle();

        LoadingDialogFragment fragment = new LoadingDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
//        dialog.setIndeterminate(true);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        return dialog;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading_dialog, container, false);
    }
}
