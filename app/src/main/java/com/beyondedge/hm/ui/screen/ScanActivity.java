package com.beyondedge.hm.ui.screen;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beyondedge.hm.R;
import com.beyondedge.hm.base.BaseActivity;
import com.beyondedge.zxingscanner.ZXingScannerViewCustom;
import com.google.zxing.Result;


/**
 * Created by Hoa Nguyen on Apr 25 2019.
 */
public class ScanActivity extends BaseActivity implements ZXingScannerViewCustom.ResultHandler {

    int numScan = 0;
    private ZXingScannerViewCustom mScannerView;
    private TextView barcode;
    private ScrollView scrollView;
    private ImageView btFlash;
    private boolean mFlash = false;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        setTitleToolbar(getString(R.string.product_barcode));
        enableBackButtonToolbar();

        ViewGroup contentFrame = findViewById(R.id.content_frame);
        barcode = findViewById(R.id.barcode);
        scrollView = findViewById(R.id.scrollView);
        btFlash = findViewById(R.id.btFlash);
        mScannerView = new ZXingScannerViewCustom(this);
        contentFrame.addView(mScannerView);
        mScannerView.setFlash(mFlash);

        btFlash.setOnClickListener(v -> {
            mFlash = !mFlash;

            if (mFlash) {
                btFlash.setImageResource(R.mipmap.ic_flash_deactive);
            } else {
                btFlash.setImageResource(R.mipmap.ic_flash_active);
            }
            mScannerView.setFlash(mFlash);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
//        Toast.makeText(this, "Contents = " + rawResult.getText() +
//                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
//
//        // Note:
//        // * Wait 2 seconds to resume the preview.
//        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
//        // * I don't know why this is the case but I don't have the time to figure out.
//        Handler handler = new Handler();
//        handler.postDelayed(() -> mScannerView.resumeCameraPreview(ScanActivity.this), 2000);

//        showMessageDialog("Contents = " + rawResult.getText() +
//                ", Format = " + rawResult.getBarcodeFormat().toString());

        StringBuilder builder = new StringBuilder(barcode.getText());
        builder.append("\n ")
                .append(++numScan)
                .append(" ---------------------------")
                .append("\n")
                .append(rawResult.getText())
                .append("\nEnd ---------------------------\n");
        barcode.setText(builder.toString());

        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 250);

        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));

        Handler handler = new Handler();
        handler.postDelayed(() -> mScannerView.resumeCameraPreview(ScanActivity.this), 1000);
    }

    public void showMessageDialog(String message) {
        showAlertDialog(message, false, (dialog, which) -> mScannerView.resumeCameraPreview(ScanActivity.this));
    }
}
