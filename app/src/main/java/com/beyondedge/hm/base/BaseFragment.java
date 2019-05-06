package com.beyondedge.hm.base;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.beyondedge.hm.R;
import com.beyondedge.hm.ui.view.LoadingDialogFragment;

/**
 * Created by Hoa Nguyen on Apr 23 2019.
 */
public class BaseFragment extends Fragment {

    private DialogFragment loadingDialog;

    protected void startActivity(Class className) {
        startActivity(new Intent(this.getActivity(), className));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.web_page_layout, container, false);
    }

    protected void showPopupError(String error) {
        new AlertDialog.Builder(getContext())
                .setMessage(error)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialogFragment.newInstance();
        } else {
            loadingDialog.dismiss();
        }

        loadingDialog.show(getChildFragmentManager(), LoadingDialogFragment.class.getSimpleName());
    }

    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
