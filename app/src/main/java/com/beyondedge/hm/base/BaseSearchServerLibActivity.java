package com.beyondedge.hm.base;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.android.materialsearchview.MaterialSearchView;
import com.beyondedge.hm.R;
import com.beyondedge.hm.searchdb.SearchServerViewModel;
import com.beyondedge.hm.searchdb.SearchSuggestRecyclerAdapter;
import com.beyondedge.hm.searchdb.server.SearchEntity;

import java.util.ArrayList;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public abstract class BaseSearchServerLibActivity extends BaseActivity implements
        MaterialSearchView.OnQueryTextListener, SearchSuggestRecyclerAdapter.SearchRecyclerInterface {
    protected QueryTextListener mQueryTextListener;
    private SearchServerViewModel model;
    private SearchSuggestRecyclerAdapter adapterSearch;
    private MaterialSearchView searchHolder;
    private boolean isShowSearchMenu = true;

    protected abstract QueryTextListener getQueryTextListener();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initSearchView() {
        model = ViewModelProviders.of(this).get(SearchServerViewModel.class);
        observeSearchList(model);
        searchHolder = findViewById(R.id.searchHolder);
        adapterSearch = new SearchSuggestRecyclerAdapter(this);
        searchHolder.addQueryTextListener(this);
        searchHolder.setSearchRecyclerAdapter(adapterSearch);
        if (searchHolder == null) {
            throw new RuntimeException("searchHolder == null in Activity - " + getClass());
        }

        mQueryTextListener = getQueryTextListener();
    }

    private void observeSearchList(SearchServerViewModel model) {
        model.getSearchListLive().observe(this, searchEntities -> {
            searchHolder.hideLoading();
            adapterSearch.setItems(searchEntities != null ? searchEntities : new ArrayList<>());
        });
    }

    protected void showHideSearchMenu(boolean isShow) {
        isShowSearchMenu = isShow;
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if (searchHolder.isVisible()) {
            searchHolder.hideSearch();
        } else {
            super.onBackPressed();
        }
    }

    // MaterialSearchView listeners
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchHolder.hideRecycler();
        searchHolder.hideSearch();

        if (mQueryTextListener != null) {
            mQueryTextListener.onQueryTextSubmit(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchHolder.showRecycler();

//        if (mQueryTextListener != null) {
//            mQueryTextListener.onQueryTextChange(newText);
//        }
        searchHolder.showLoading();
        model.searchQuery(newText);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_search).setVisible(isShowSearchMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            searchHolder.showSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface QueryTextListener {
        void onQueryTextSubmit(String query);

        void onQueryTextChange(String newText);
    }
}
