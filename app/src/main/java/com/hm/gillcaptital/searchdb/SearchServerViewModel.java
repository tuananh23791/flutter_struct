package com.hm.gillcaptital.searchdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.hm.gillcaptital.base.BaseViewModel;
import com.hm.gillcaptital.searchdb.server.SearchEntity;
import com.hm.gillcaptital.searchdb.server.SearchServerRepository;

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

    //no more use| now is search by RxJava on onQueryTextChange(String text) function
    public void searchQuery(String query) {
        repository.searchQuery(getApplication(), getMainLiveData(), query);
    }

    private void settingRxSearch() {
        subscribe = subject
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(s -> !s.isEmpty())
                .distinctUntilChanged()

                .switchMap((Function<String, Observable<ArrayList<SearchEntity>>>) query -> {
                    Timber.d(query);
                    return repository.searchQuery(getApplication(), query).onErrorReturnItem(new ArrayList<>());
                })
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

    public void onQueryTextChange(String text) {
        if (subscribe != null && !subscribe.isDisposed())
            subject.onNext(text);
        else
            Timber.d("Edittext: %s subscribe isDisposed", text);
    }

    public void onQueryTextSubmit() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subject.onComplete();
            subscribe.dispose();
        } else
            Timber.d("Edittext: subscribe isDisposed");
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
