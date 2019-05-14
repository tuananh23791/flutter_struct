package com.beyondedge.hm.searchdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.beyondedge.hm.base.BaseViewModel;
import com.beyondedge.hm.searchdb.server.SearchEntity;
import com.beyondedge.hm.searchdb.server.SearchServerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class SearchServerViewModel extends BaseViewModel<List<SearchEntity>> {
    final PublishSubject<String> subject = PublishSubject.create();
    private SearchServerRepository repository;
    private Disposable subscribe;

    public SearchServerViewModel(@NonNull Application application) {
        super(application);
        disableLoading();
        repository = SearchServerRepository.getInstance();
        settingRxSearch();
    }

    public LiveData<List<SearchEntity>> getSearchListLive() {
        return getMainLiveData();
    }

    public void searchQuery(String query) {
        repository.searchQuery(getMainLiveData(), query);
    }

    private void settingRxSearch() {
        subscribe = subject
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(s -> !s.isEmpty())
                .distinctUntilChanged()
                .switchMap((Function<String, Observable<ArrayList<SearchEntity>>>) query ->
                        repository.searchQuery(query))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Timber.d(result.toString());
                    getMainLiveData().postValue(result);
                }, throwable -> {
                    Timber.d(throwable.toString());
                    getMainLiveData().postValue(new ArrayList<>());
                })
        ;
    }

    public void onQueryTextSubmit() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subject.onComplete();
            subscribe.dispose();
        }
    }

    public void onQueryTextChange(String text) {
        if (subscribe != null && !subscribe.isDisposed())
            subject.onNext(text);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.clear();
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
    }

}
