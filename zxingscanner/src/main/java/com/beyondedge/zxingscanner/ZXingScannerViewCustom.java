package com.beyondedge.zxingscanner;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import me.dm7.barcodescanner.core.BarcodeScannerView;
import me.dm7.barcodescanner.core.DisplayUtils;

/**
 * Created by Hoa Nguyen on May 07 2019.
 */
public class ZXingScannerViewCustom extends BarcodeScannerView {
    private static final String TAG = "ZXingScannerView";

    private HM_ITFReader mHM_itfReader = new HM_ITFReader();
    private ResultHandler mResultHandler;

    public ZXingScannerViewCustom(Context context) {
        super(context);
    }

    public ZXingScannerViewCustom(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setResultHandler(ResultHandler resultHandler) {
        mResultHandler = resultHandler;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mResultHandler == null) {
            return;
        }

        try {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            int width = size.width;
            int height = size.height;

            if (DisplayUtils.getScreenOrientation(getContext()) == Configuration.ORIENTATION_PORTRAIT) {
                int rotationCount = getRotationCount();
                if (rotationCount == 1 || rotationCount == 3) {
                    int tmp = width;
                    width = height;
                    height = tmp;
                }
                data = getRotatedData(data, camera);
            }

            Result rawResult = null;
            PlanarYUVLuminanceSource source = buildLuminanceSource(data, width, height);

            if (source != null) {
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                try {
                    rawResult = mHM_itfReader.decode(bitmap);
                } catch (ReaderException re) {
                    // continue
                } catch (NullPointerException npe) {
                    // This is terrible
                } catch (ArrayIndexOutOfBoundsException aoe) {

                } finally {
                    mHM_itfReader.reset();
                }

                if (rawResult == null) {
                    LuminanceSource invertedSource = source.invert();
                    bitmap = new BinaryBitmap(new HybridBinarizer(invertedSource));
                    try {
                        rawResult =  mHM_itfReader.decode(bitmap);
                    } catch (NotFoundException e) {
                        // continue
                    } catch (FormatException e) {
                        // continue
                    } finally {
                        mHM_itfReader.reset();
                    }
                }
            }

            final Result finalRawResult = rawResult;

            if (finalRawResult != null) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Stopping the preview can take a little long.
                        // So we want to set result handler to null to discard subsequent calls to
                        // onPreviewFrame.
                        ResultHandler tmpResultHandler = mResultHandler;
                        mResultHandler = null;

                        stopCameraPreview();
                        if (tmpResultHandler != null) {
                            tmpResultHandler.handleResult(finalRawResult);
                        }
                    }
                });
            } else {
                camera.setOneShotPreviewCallback(this);
            }
        } catch (RuntimeException e) {
            // TODO: Terrible hack. It is possible that this method is invoked after camera is released.
            Log.e(TAG, e.toString(), e);
        }
    }

    public void resumeCameraPreview(ResultHandler resultHandler) {
        mResultHandler = resultHandler;
        super.resumeCameraPreview();
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview(width, height);
        if (rect == null) {
            return null;
        }
        // Go ahead and assume it's YUV rather than die.
        PlanarYUVLuminanceSource source = null;

        try {
            source = new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                    rect.width(), rect.height(), false);
        } catch (Exception e) {
        }

        return source;
    }

    public interface ResultHandler {
        void handleResult(Result rawResult);
    }
}
