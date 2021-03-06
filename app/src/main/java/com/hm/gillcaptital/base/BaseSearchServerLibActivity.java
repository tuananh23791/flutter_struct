package com.hm.gillcaptital.base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import com.android.materialsearchview.MaterialSearchView;
import com.hm.gillcaptital.BuildConfig;
import com.hm.gillcaptital.R;
import com.hm.gillcaptital.config.HMConfig;
import com.hm.gillcaptital.config.LoadConfig;
import com.hm.gillcaptital.searchdb.SearchServerViewModel;
import com.hm.gillcaptital.searchdb.SearchSuggestRecyclerAdapter;
import com.hm.gillcaptital.searchdb.server.SearchEntity;
import com.hm.gillcaptital.ui.screen.PageWebActivity;
import com.hm.gillcaptital.ui.screen.ScanActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public abstract class BaseSearchServerLibActivity extends BaseActivity implements
        MaterialSearchView.OnQueryTextListener, SearchSuggestRecyclerAdapter.SearchRecyclerInterface {
    private static final int REQUEST_TAKE_PHOTO = 109;

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private static final int PERMISSIONS_REQUEST_CAMERA = 101;
    private static final int PERMISSIONS_REQUEST_PICK = 141;
    private static final int ACTION_UNKNOWN = -1;
    private static final int ACTION_PICK = 1;
    private static final int ACTION_TAKE_PICTURE = 2;
    private static final int ACTION_SCAN = 3;
    protected QueryTextListener mQueryTextListener;
    private SearchServerViewModel model;
    private SearchSuggestRecyclerAdapter adapterSearch;
    private MaterialSearchView searchHolder;
    private View searchPaddingView;
    private int mRequestCode;
    private int mImageAction;
    private String mCurrentPhotoPath;

    protected abstract QueryTextListener getQueryTextListener();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    boolean isSearchInit() {
        return searchHolder != null;
    }

    protected void initSearchView() {
        HMConfig config = LoadConfig.getInstance(this).load();
        model = ViewModelProviders.of(this).get(SearchServerViewModel.class);
        observeSearchList(model);
        searchHolder = findViewById(R.id.searchHolder);
        //TODO enable later
        searchHolder.enableSearchPicture(false);
        searchPaddingView = findViewById(R.id.viewTempPadding);
        //Comment - All now using back in activity tool_bar
        searchHolder.canBack(false);
        searchHolder.setSearchHint(config.getLanguageBy("search_product"), false);
        adapterSearch = new SearchSuggestRecyclerAdapter(this);
        searchHolder.addQueryTextListener(this);
        searchHolder.setSearchRecyclerAdapter(adapterSearch);
        if (searchHolder == null) {
            throw new RuntimeException("searchHolder == null in Activity - " + getClass());
        }

        mQueryTextListener = getQueryTextListener();
    }

    protected void settingBack(boolean can) {
        //Comment - All now using back in activity tool_bar
        //searchHolder.settingBack(can);
        if (searchPaddingView != null) {
            searchPaddingView.setVisibility(can ? View.VISIBLE : View.GONE);
        }

        View backBt = findViewById(R.id.btn_back);
        if (backBt != null) {
            backBt.setVisibility(can ? View.VISIBLE : View.GONE);
        }
    }

    protected void showSearch() {
        if (!searchHolder.isVisible())
            searchHolder.showSearch();
    }

    protected void hideSearch() {
        if (searchHolder.isVisible())
            searchHolder.hideSearch();
        else
            searchHolder.hideKeyboard();
    }

    @Override
    protected void enableBackButtonToolbar(View.OnClickListener clickListener) {
        super.enableBackButtonToolbar(clickListener);
    }

    protected boolean isSearchVisible() {
        return searchHolder.isVisible();
    }

    protected void hideKeyboard() {
        searchHolder.hideKeyboard();
    }

    protected boolean isVisible() {
        return searchHolder.isVisible();
    }


    private void observeSearchList(SearchServerViewModel model) {
        model.getSearchListLive().observe(this, searchEntities -> {
            searchHolder.hideLoading();
            adapterSearch.setItems(searchEntities != null ? searchEntities : new ArrayList<>());
        });
    }

    // MaterialSearchView listeners
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchHolder.hideRecycler();

//        model.onQueryTextSubmit();

        if (mQueryTextListener != null) {
            mQueryTextListener.onQueryTextSubmit(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            searchHolder.hideLoading();
            searchHolder.hideRecycler();
        } else {
            searchHolder.showRecycler();

            searchHolder.showLoading();
            //search on RxJava
            model.onQueryTextChange(newText);

            //no more use
//        model.searchQuery(newText);
        }
        return true;
    }

    // adapter item clicked
    @Override
    public void onSearchItemClicked(String query) {
        searchHolder.setSearchText(query);
        searchHolder.hideRecycler();
        onQueryTextSubmit(query);
    }

    @Override
    public void onSearchDeleteClicked(SearchEntity searchEntity) {
        //TODO
    }

    //--Search Take Picture

    private void takePictureWithPermission(int requestCode) {
        this.mImageAction = ACTION_TAKE_PICTURE;
        this.mRequestCode = requestCode;

        if (checkCameraImagePermission()) {
            takePictureWithPermission();
        }
    }

    private void takeScanWithPermission(int requestCode) {
        this.mImageAction = ACTION_SCAN;
        this.mRequestCode = requestCode;

        if (checkCameraImagePermission()) {
            startActivity(ScanActivity.class);
        }
    }

    private void takePictureWithPermission() {
        try {
            dispatchTakePictureIntent();
        } catch (IOException e) {
            //TODO sho popup error
            cleanData();
            e.printStackTrace();
        }
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            } else {
                Toast.makeText(this, "Cannot create file!!!", Toast.LENGTH_SHORT).show();
                //TODO sho popup error
                cleanData();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //https://android.jlelse.eu/androids-new-image-capture-from-a-camera-using-file-provider-dd178519a954
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DCIM), "Camera");

        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void cleanData() {
        mRequestCode = 0;
        mImageAction = ACTION_UNKNOWN;
    }

    private boolean checkCameraImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CAMERA);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
//            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
//                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    if (mImageAction == ACTION_PICK) {
//                        pickImage(this, PERMISSIONS_REQUEST_PICK);
//                    } else if (mImageAction == ACTION_PICK_PDF) {
//                        pickPDF(this, PERMISSIONS_REQUEST_PICK_PDF);
//                    }
//                } else {
//                    showRequestError(getString(R.string.permission_media_file_note));
//                }
//                break;

            case PERMISSIONS_REQUEST_CAMERA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (mImageAction == ACTION_TAKE_PICTURE) {
                        takePictureWithPermission();
                    } else if (mImageAction == ACTION_SCAN) {
                        startActivity(ScanActivity.class);
                    }
                } else {
                    //TODO
                    Toast.makeText(this, getString(R.string.permission_camera_note), Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PERMISSIONS_REQUEST_PICK) {
//            if (resultCode == RESULT_OK) {
//                if (isCrop)
//                    beginCrop(data.getData());
//                else {
//                    if (isScale) {
//                        scaleImage(data);
//                    } else {
//                        onImageBack(mRequestCode, RESULT_OK, data);
//                        cleanData();
//                    }
//                }
//            } else {
//                onImageBack(mRequestCode, RESULT_CANCELED, null);
//                cleanData();
//            }
//        }
//        else
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                File file = new File(imageUri.getPath());
                if (file.exists()) {
                    showAlertDialog("File: " + file.getPath(), true, null);
                }
            } else {
                //TODO
//                onImageBack(mRequestCode, RESULT_CANCELED, null);
                cleanData();
            }
        }
    }

    @Override
    public void onActionSearch(MaterialSearchView.ActionSearch type /*0 - Picture ,1 - Barcode */) {
        if (type == MaterialSearchView.ActionSearch.Picture) {
            takePictureWithPermission(589);
        } else if (type == MaterialSearchView.ActionSearch.Barcode) {
//            if (BuildConfig.DEBUG && BuildConfig.LOG) {
//                showTestPopup();
//            } else {
            takeScanWithPermission(900);
//            }
        }
    }

    private void showTestPopup() {
        if (BuildConfig.DEBUG && BuildConfig.LOG) {
            final String[] data = {
                    "http://sharefile.beyondedge.com.sg/hm/id/checkout.html",
                    "http://sharefile.beyondedge.com.sg/hm/id/productCate.html",
                    "http://sharefile.beyondedge.com.sg/hm/id/productCateShare.html",
                    "http://sharefile.beyondedge.com.sg/hm/id/product%20detail.html",
                    "http://sharefile.beyondedge.com.sg/hm/id/checkout.html",
                    "http://sharefile.beyondedge.com.sg/hm/id/profile.html",
            };
            new AlertDialog.Builder(this)
                    .setSingleChoiceItems(data, 0, (dialog, which) -> {
                        PageWebActivity.startScreen(BaseSearchServerLibActivity.this,
                                data[which], "");
                        dialog.dismiss();
                    })
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public interface QueryTextListener {
        void onQueryTextSubmit(String query);

        void onQueryTextChange(String newText);
    }
}
