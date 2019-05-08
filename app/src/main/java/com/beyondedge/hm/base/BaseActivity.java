package com.beyondedge.hm.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.beyondedge.hm.R;

/**
 * Created by Hoa Nguyen on Apr 20 2019.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        settingView();
    }

    protected void settingView() {
        Toolbar toolbar = findViewById(R.id.tool_bar);

        if (toolbar != null)
            setSupportActionBar(toolbar);
    }

    public void setTitleToolbar(String title) {
        TextView textTitle = findViewById(R.id.txt_title);
        if (textTitle != null) {
            textTitle.setVisibility(View.VISIBLE);
            textTitle.setText(title);
        }
    }

    protected void enableBackButtonToolbar(View.OnClickListener clickListener) {
        View backBt = findViewById(R.id.btn_back);
        if (backBt != null) {
            backBt.setVisibility(View.VISIBLE);
            if (clickListener != null) {
                backBt.setOnClickListener(clickListener);
            } else {
                backBt.setOnClickListener(v -> super.onBackPressed());
            }
        }
    }

    protected void startActivity(Class className) {
        startActivity(new Intent(this, className));
    }

    protected void showAlertDialog(String mess, boolean canCancel, DialogInterface.OnClickListener OKlistener) {
        new AlertDialog.Builder(this)
                .setMessage(mess)
                .setCancelable(canCancel)
                .setPositiveButton(android.R.string.ok, OKlistener)
                .show();
    }

}
