package com.androidtitan.culturedapp.common;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by amohnacs on 11/20/16.
 */

public class RxHelper {

    private static final Observable.Transformer<Observable, Observable> schedulersTransformer =
            observable -> observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }
}
