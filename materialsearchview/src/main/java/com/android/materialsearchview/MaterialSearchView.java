package com.android.materialsearchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.android.materialsearchview.databinding.ViewSearchBinding;

public class MaterialSearchView extends CardView {
    static final String LOG_TAG = "MaterialSearchView";
    private static final int ANIMATION_DURATION = 250;
    boolean canBack = false;
    private boolean animateSearchView;
    private int searchMenuPosition;
    private String searchHint;
    private int searchTextColor;
    private Integer searchIconColor;
    private CharSequence mOldQueryText;
    private CharSequence mUserQuery;
    private boolean hasAdapter = false;
    private boolean hideSearch = false;
    private ViewSearchBinding b;
    private OnQueryTextListener listenerQuery;
    private final TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            onSubmitQuery();
            return true;
        }
    };
    private OnVisibilityListener visibilityListener;
    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == b.imgClear) {
                setSearchText(null);
                hideKeyboard();
            } else if (v == b.imgBack) {
                hideSearch();
            } else if (v == b.imgSearch) {
                b.editText.requestFocus();
            }
        }
    };
    /**
     * Callback to watch the text field for empty/non-empty
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int before, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int after) {
            submitText(s);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private boolean enableSearchPicture = false;
    private boolean enableSearchScanBarcode = false;

    public MaterialSearchView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public MaterialSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaterialSearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        if (view != null && view.isFocused()) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @BindingAdapter("dividerDirection")
    public static void dividerDirection(RecyclerView recyclerView, boolean isDividerDirection) {
        if (isDividerDirection) {
            RecyclerView.ItemDecoration decoration =
                    new DividerItemDecoration(recyclerView.getContext(), RecyclerView.VERTICAL);
            recyclerView.addItemDecoration(decoration);
        }
    }

    public void enableSearchPicture(boolean enableSearchPicture) {
        this.enableSearchPicture = enableSearchPicture;

        if (enableSearchPicture) {
            b.imgPicture.setVisibility(VISIBLE);
            b.imgPicture.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenerQuery.onActionSearch(ActionSearch.Picture);
                }
            });
        } else {
            b.imgPicture.setVisibility(GONE);
        }
    }

    public void enableSearchScanBarcode(boolean enable) {
        this.enableSearchScanBarcode = enable;

        if (enable) {
            b.imgBarcode.setVisibility(VISIBLE);
            b.imgBarcode.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenerQuery.onActionSearch(ActionSearch.Barcode);
                }
            });
        } else {
            b.imgBarcode.setVisibility(GONE);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MaterialSearchView, 0, 0);

        final LayoutInflater inflater = LayoutInflater.from(context);
        b = DataBindingUtil.inflate(inflater, R.layout.view_search, this, true);
        animateSearchView = a.getBoolean(R.styleable.MaterialSearchView_search_animate, false);
        searchMenuPosition = a.getInteger(R.styleable.MaterialSearchView_search_menu_position, 0);
        searchHint = a.getString(R.styleable.MaterialSearchView_search_hint);
        searchTextColor = a.getColor(R.styleable.MaterialSearchView_search_text_color, getResources().getColor(android.R.color.black));
        searchIconColor = a.getColor(R.styleable.MaterialSearchView_search_icon_color, getResources().getColor(android.R.color.black));

        b.imgSearch.setOnClickListener(mOnClickListener);

        b.imgBack.setOnClickListener(mOnClickListener);
        b.imgClear.setOnClickListener(mOnClickListener);
        b.editText.addTextChangedListener(mTextWatcher);
        b.editText.setOnEditorActionListener(mOnEditorActionListener);

        final int imeOptions = a.getInt(R.styleable.MaterialSearchView_search_ime_options, -1);
        if (imeOptions != -1) {
            setImeOptions(imeOptions);
        }

        final int inputType = a.getInt(R.styleable.MaterialSearchView_search_input_type, -1);
        if (inputType != -1) {
            setInputType(inputType);
        }

        boolean focusable;
        focusable = a.getBoolean(R.styleable.MaterialSearchView_search_focusable, true);
        b.editText.setFocusable(focusable);

        a.recycle();

        b.editText.setHint(getSearchHint());
        b.editText.setTextColor(getTextColor());
        setDrawableTint(b.imgBack.getDrawable(), searchIconColor);
        setDrawableTint(b.imgClear.getDrawable(), searchIconColor);
        enableSearchPicture(false);
        enableSearchScanBarcode(false);
        checkForAdapter();

        canBack(canBack);
    }

    public void canBack(boolean canBack) {
        this.canBack = canBack;
        b.imgBack.setVisibility(canBack ? VISIBLE : GONE);
    }

    public void showLoading() {
        if (b.recycler.getAdapter() != null && b.recycler.getAdapter().getItemCount() <= 0) {
            b.frameLoading.setVisibility(VISIBLE);
            b.recycler.setVisibility(GONE);
        }
    }

    public void hideLoading() {
        b.frameLoading.setVisibility(GONE);
        b.recycler.setVisibility(VISIBLE);
    }

    private void submitText(CharSequence s) {
        mUserQuery = b.editText.getText();
        updateClearButton();
        if (listenerQuery != null && !TextUtils.equals(s, mOldQueryText)) {
            listenerQuery.onQueryTextChange(String.valueOf(s));
        }
        mOldQueryText = s.toString();
    }

    void onSubmitQuery() {
        if (listenerQuery != null) {
            b.linearItemsHolder.setVisibility(GONE);
            listenerQuery.onQueryTextSubmit(b.editText.getText().toString());
        }
    }

    private void updateClearButton() {
        final boolean hasText = !TextUtils.isEmpty(b.editText.getText());
        b.imgClear.setVisibility(hasText ? VISIBLE : GONE);
    }

    public void setQuery(CharSequence query, boolean submit) {
        b.editText.setText(query);
        if (query != null) {
            mUserQuery = query;
        }
        // If the query is not empty and submit is requested, submit the query
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    /**
     * Returns the IME options set on the query text field.
     */
    public int getImeOptions() {
        return b.editText.getImeOptions();
    }

    /**
     * Sets the IME options on the query text field.
     */
    public void setImeOptions(int imeOptions) {
        b.editText.setImeOptions(imeOptions);
    }

    /**
     * Returns the input type set on the query text field.
     */
    public int getInputType() {
        return b.editText.getInputType();
    }

    /**
     * Sets the input type on the query text field.
     */
    public void setInputType(int inputType) {
        b.editText.setInputType(inputType);
    }

    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    public void setSearchText(String queryText) {
        b.editText.setText(queryText);
    }

    public void setSearchRecyclerAdapter(RecyclerView.Adapter adapter) {
        b.recycler.setAdapter(adapter);
        checkForAdapter();
    }

    public void hideRecycler() {
        hideSearch = true;
        b.linearItemsHolder.setVisibility(GONE);
    }

    public void showRecycler() {
        hideSearch = false;
        b.linearItemsHolder.setVisibility(VISIBLE);
    }

    public void showSearch() {
        hideSearch = false;
        checkForAdapter();
        if (animateSearchView) {
            if (Build.VERSION.SDK_INT >= 21) {
                Animator animatorShow = ViewAnimationUtils.createCircularReveal(
                        this, // view
                        getCenterX(), // center x
                        (int) convertDpToPixel(23), // center y
                        0, // start radius
                        (float) Math.hypot(getWidth(), getHeight()) // end radius
                );
                animatorShow.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (visibilityListener != null) {
                            visibilityListener.onOpen();
                        }
                        if (hasAdapter) {
                            b.linearItemsHolder.setVisibility(View.VISIBLE);
                        }
                    }
                });
                animatorShow.start();
            } else {
                if (hasAdapter) {
                    b.linearItemsHolder.setVisibility(View.VISIBLE);
                }
            }
            setVisibility(VISIBLE);
        } else {
            postVisible(View.VISIBLE);
        }
    }

    private void postVisible(final int visibleType) {
        post(new Runnable() {
            @Override
            public void run() {
                setVisibility(visibleType);
            }
        });
    }

    public void hideKeyboard() {
        hideKeyboardFrom(getContext(), b.editText);
    }

    public void hideSearch() {
        hideKeyboardFrom(getContext(), b.editText);
        checkForAdapter();
        if (hasAdapter) {
            b.linearItemsHolder.setVisibility(View.GONE);
        }
        if (animateSearchView) {
            if (Build.VERSION.SDK_INT >= 21) {
                Animator animatorHide = ViewAnimationUtils.createCircularReveal(
                        this, // View
                        getCenterX(), // center x
                        (int) convertDpToPixel(23), // center y
                        (float) Math.hypot(getWidth(), getHeight()), // start radius
                        0// end radius
                );
                animatorHide.setStartDelay(hasAdapter ? ANIMATION_DURATION : 0);
                animatorHide.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (visibilityListener != null) {
                            visibilityListener.onClose();
                        }
                        setVisibility(GONE);
                    }
                });
                animatorHide.start();
            } else {
                setVisibility(GONE);
            }
        } else {
            postVisible(View.GONE);
        }
    }

    public void setMenuPosition(int menuPosition) {
        this.searchMenuPosition = menuPosition;
        invalidate();
        requestFocus();
    }

    // search searchHint
    public String getSearchHint() {
        if (TextUtils.isEmpty(searchHint)) {
            return "Search";
        }
        return searchHint;
    }

    public void setSearchHint(String searchHint, boolean isFocus) {
        this.searchHint = searchHint;
        b.editText.setHint(this.searchHint);
        invalidate();
        if (isFocus)
            requestFocus();
    }

    public void setSearchIconColor(int searchIconColor) {
        this.searchIconColor = searchIconColor;
        invalidate();
        requestFocus();
    }

    public int getTextColor() {
        return searchTextColor;
    }

    // text color
    public void setTextColor(int textColor) {
        this.searchTextColor = textColor;
        invalidate();
        requestFocus();
    }

    /**
     * Get views
     */
    public EditText getEditText() {
        return b.editText;
    }

    public ImageView getImageBack() {
        return b.imgBack;
    }

    public ImageView getImageClear() {
        return b.imgClear;
    }

    public RecyclerView getRecyclerView() {
        return b.recycler;
    }

    /**
     * Interface
     */
    public void addQueryTextListener(OnQueryTextListener listenerQuery) {
        this.listenerQuery = listenerQuery;
    }

    public void setOnVisibilityListener(OnVisibilityListener visibilityListener) {
        this.visibilityListener = visibilityListener;
    }

    /**
     * Helpers
     */
    public void setDrawableTint(Drawable resDrawable, int resColor) {
        resDrawable.setColorFilter(new PorterDuffColorFilter(resColor, PorterDuff.Mode.SRC_ATOP));
        resDrawable.mutate();
    }

    public float convertDpToPixel(float dp) {
        return dp * (getContext().getResources().getDisplayMetrics().densityDpi / 160f);
    }

    private void checkForAdapter() {
        hasAdapter = !hideSearch && b.recycler.getAdapter() != null && b.recycler.getAdapter().getItemCount() > 0;
    }

    /*
    TODO not correct but close
    Need to do correct measure
     */
    private int getCenterX() {
        int icons = (int) (getWidth() - convertDpToPixel(21 * (1 + searchMenuPosition)));
        int padding = (int) convertDpToPixel(searchMenuPosition * 21);
        return icons - padding;
    }

    public enum ActionSearch {
        Picture, Barcode
    }

    public interface OnQueryTextListener {
        boolean onQueryTextSubmit(String query);

        boolean onQueryTextChange(String newText);

        void onActionSearch(ActionSearch type /*0 - Picture ,1 - Barcode */);
    }

    public interface OnVisibilityListener {
        boolean onOpen();

        boolean onClose();
    }
}
