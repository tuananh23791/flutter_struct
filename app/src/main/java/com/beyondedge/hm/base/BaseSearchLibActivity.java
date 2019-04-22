package com.beyondedge.hm.base;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.materialsearchview.MaterialSearchView;
import com.beyondedge.hm.R;
import com.beyondedge.hm.searchdb.SearchRecyclerAdapter;
import com.beyondedge.hm.searchdb.SearchViewModel;
import com.beyondedge.hm.searchdb.db.SearchEntity;

import java.util.List;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public abstract class BaseSearchLibActivity extends BaseActivity implements
        MaterialSearchView.OnQueryTextListener, SearchRecyclerAdapter.SearchRecyclerInterface {
    private SearchViewModel model;
    private SearchRecyclerAdapter adapterSearch;
    private MaterialSearchView searchHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initSearchView() {
        model = ViewModelProviders.of(this).get(SearchViewModel.class);
        observeSearchList(model);
        searchHolder = findViewById(R.id.searchHolder);
        adapterSearch = new SearchRecyclerAdapter(this);
        searchHolder.addQueryTextListener(this);
        searchHolder.setSearchRecyclerAdapter(adapterSearch);
        if (searchHolder == null) {
            throw new RuntimeException("searchHolder == null in Activity - " + getClass());
        }
    }

    private void observeSearchList(SearchViewModel model) {
        model.getSearchListLive().observe(this, new Observer<List<SearchEntity>>() {
            @Override
            public void onChanged(@Nullable List<SearchEntity> searchEntities) {
                adapterSearch.setItems(searchEntities);
            }
        });
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
        model.insertSearchEntity(query);
        //TODO query
//        model.searchFood(query, 0);
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        searchHolder.hideSearch();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchHolder.showRecycler();
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
        model.deleteSearchEntity(searchEntity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
