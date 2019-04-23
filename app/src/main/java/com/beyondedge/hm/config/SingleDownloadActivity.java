package com.beyondedge.hm.config;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.beyondedge.hm.R;
import com.beyondedge.hm.base.BaseActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2core.Extras;
import com.tonyodev.fetch2core.FetchObserver;
import com.tonyodev.fetch2core.Func;
import com.tonyodev.fetch2core.MutableExtras;
import com.tonyodev.fetch2core.Reason;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.ref.WeakReference;

import timber.log.Timber;

import static com.beyondedge.hm.config.Constant.IS_FORCE_LOCAL_CONFIG;

/**
 * Created by Hoa Nguyen on Apr 23 2019.
 */
public class SingleDownloadActivity extends BaseActivity implements FetchObserver<Download> {

    private static final int STORAGE_PERMISSION_CODE = 100;

    private View mainView;
    private TextView progressTextView;
    private TextView titleTextView;
    private TextView etaTextView;
    private TextView value;
    private TextView downloadSpeedTextView;
    private Request request;
    private Fetch fetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_download);
        mainView = findViewById(R.id.activity_single_download);
        progressTextView = findViewById(R.id.progressTextView);
        titleTextView = findViewById(R.id.titleTextView);
        etaTextView = findViewById(R.id.etaTextView);
        value = findViewById(R.id.value);
        downloadSpeedTextView = findViewById(R.id.downloadSpeedTextView);
        if (IS_FORCE_LOCAL_CONFIG) {
            readConfig();
        } else {
            fetch = Fetch.Impl.getDefaultInstance();
            checkStoragePermission();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (request != null) {
            fetch.attachFetchObserversForDownload(request.getId(), this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (request != null) {
            fetch.removeFetchObserversForDownload(request.getId(), this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fetch.close();
    }

    @Override
    public void onChanged(Download data, @NotNull Reason reason) {
        updateViews(data, reason);
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            enqueueDownload();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enqueueDownload();
        } else {
            Snackbar.make(mainView, R.string.permission_not_enabled, Snackbar.LENGTH_LONG).show();
        }
    }

    private void enqueueDownload() {
        final String url = Constant.LINK_CONFIG;
        final String filePath = Constant.getLinkSavedFile();
        request = new Request(url, filePath);
        request.addHeader("Authorization", getAuthorizationParam());
//        request.setExtras(getExtrasForRequest(request));

        fetch.attachFetchObserversForDownload(request.getId(), this)
                .enqueue(request, new Func<Request>() {
                    @Override
                    public void call(@NotNull Request result) {
                        request = result;
                    }
                }, new Func<Error>() {
                    @Override
                    public void call(@NotNull Error result) {
                        Timber.d("SingleDownloadActivity Error: %1$s", result.toString());
                    }
                });
    }

    private Extras getExtrasForRequest(Request request) {
        final MutableExtras extras = new MutableExtras();
        extras.putBoolean("testBoolean", true);
        extras.putString("testString", "test");
        extras.putFloat("testFloat", Float.MIN_VALUE);
        extras.putDouble("testDouble", Double.MIN_VALUE);
        extras.putInt("testInt", Integer.MAX_VALUE);
        extras.putLong("testLong", Long.MAX_VALUE);
        return extras;
    }

    private void updateViews(@NotNull Download download, Reason reason) {
        if (request.getId() == download.getId()) {
            if (reason == Reason.DOWNLOAD_QUEUED) {
                setTitleView(download.getFile());
            }
            setProgressView(download.getStatus(), download.getProgress());
            etaTextView.setText(Utils.getETAString(this, download.getEtaInMilliSeconds()));
            downloadSpeedTextView.setText(Utils.getDownloadSpeedString(this, download.getDownloadedBytesPerSecond()));
            if (download.getError() != Error.NONE) {
                showDownloadErrorSnackBar(download.getError());
            }

            if (reason == Reason.DOWNLOAD_COMPLETED) {
                final Uri uri = Uri.parse(download.getFile());
                titleTextView.setText(uri.getLastPathSegment() + " -- DOWNLOAD_COMPLETED");
                readConfig();

            }
        }
    }

    private void readConfig() {
        new ParseFileAsyncTask(this).setTextView(value).execute();
    }

    private void setTitleView(@NonNull final String fileName) {
        final Uri uri = Uri.parse(fileName);
        titleTextView.setText(uri.getLastPathSegment());
    }

    private void setProgressView(@NonNull final Status status, final int progress) {
        switch (status) {
            case QUEUED: {
                progressTextView.setText(R.string.queued);
                break;
            }
            case ADDED: {
                progressTextView.setText(R.string.added);
                break;
            }
            case DOWNLOADING:
            case COMPLETED: {
                if (progress == -1) {
                    progressTextView.setText(R.string.downloading);
                } else {
                    final String progressString = getResources().getString(R.string.percent_progress, progress);
                    progressTextView.setText(progressString);
                }
                break;
            }
            default: {
                progressTextView.setText(R.string.status_unknown);
                break;
            }
        }
    }

    private void showDownloadErrorSnackBar(@NotNull Error error) {
        final Snackbar snackbar = Snackbar.make(mainView, "Download Failed: ErrorCode: " + error, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.retry, v -> {
            fetch.retry(request.getId());
            snackbar.dismiss();
        });
        snackbar.show();
    }

    private String getAuthorizationParam() {
        String username = "hm";
        String password = "Hm@123456";
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            String credentials = username + ":" + password;
            return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        }

        return "";
    }

    private static class ParseFileAsyncTask extends AsyncTask<Void, Void, HMConfig> {
        final String filePath = Constant.getLinkSavedFile();
        final Context mContext;
        private boolean isValidSavedFile = false;
        private Gson mGson = new Gson();

        private WeakReference<TextView> textViewRef;

        private ParseFileAsyncTask(Context context) {
            mContext = context.getApplicationContext();
        }

        public ParseFileAsyncTask setTextView(TextView textView) {
            textViewRef = new WeakReference<>(textView);
            return this;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            File file = new File(filePath);
            isValidSavedFile = file.exists();
        }

        @Override
        protected HMConfig doInBackground(Void... input) {
            HMConfig result = null;
            String value;
            if (IS_FORCE_LOCAL_CONFIG) {
                value = Utils.loadJSONFromAsset(mContext);
            } else {
                if (isValidSavedFile) {
                    value = Utils.loadJSONFromDir(filePath);
                } else {
                    value = Utils.loadJSONFromAsset(mContext);
                }
            }
            try {
                result = mGson.fromJson(value, HMConfig.class);
            } catch (JsonSyntaxException e) {
                Timber.e("------------H&M------------- parse Config error");
//                Timber.e(value);
                Timber.e("------------------------- parse Config error");
                Timber.e(e);
            }

            return result;
        }

        @Override
        protected void onPostExecute(HMConfig hmConfig) {
            super.onPostExecute(hmConfig);
            if (textViewRef != null) {
                TextView textView = textViewRef.get();
                if (textView != null) {
                    textView.setText(hmConfig != null ? mGson.toJson(hmConfig) : "Config == null");
                }
            }

            LoadConfig.getInstance().setHMConfig(hmConfig);
        }
    }
}
