package com.beyondedge.hm.searchdb.server;

import com.android.materialsearchview.MaterialSearchView;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Hoa Nguyen on May 13 2019.
 */
public class RxSearchObservable {
    public static Observable<String> fromView(MaterialSearchView searchView) {

        final PublishSubject<String> subject = PublishSubject.create();

        searchView.addQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                subject.onComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                subject.onNext(text);
                return true;
            }

            @Override
            public void onActionSearch(MaterialSearchView.ActionSearch type) {
                //nothing to do here
            }
        });

        return subject;
    }
}
