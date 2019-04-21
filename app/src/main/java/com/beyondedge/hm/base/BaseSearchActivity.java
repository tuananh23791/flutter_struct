package com.beyondedge.hm.base;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;

import com.beyondedge.hm.R;

/**
 * Created by Hoa Nguyen on Apr 20 2019.
 */
public abstract class BaseSearchActivity extends BaseActivity {
    private static final String TAG = "BaseSearchActivity";

    protected abstract SearchView.OnCloseListener getSearchOnCloseListener();

    protected abstract SearchView.OnQueryTextListener getSearchOnQueryTextListener();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.LTGRAY);
        searchAutoComplete.setTextColor(Color.GRAY);

        View searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchPlate.setBackgroundColor(getResources().getColor(R.color.colorWhite));

        ImageView searchCloseIcon = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchCloseIcon.setColorFilter(getResources().getColor(R.color.colorBlack));

        ImageView searchIcon = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setColorFilter(getResources().getColor(R.color.colorBlack));


//        ImageView voiceIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_voice_btn);
//        voiceIcon.setImageResource(R.drawable.abc_ic_voice_search);
//
//        ImageView searchIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
//        searchIcon.setImageResource(R.drawable.abc_ic_search);

        searchView.setOnCloseListener(getSearchOnCloseListener());
        searchView.setOnQueryTextListener(getSearchOnQueryTextListener());

        return true;
    }
}
