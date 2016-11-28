package com.cloudbees.lxd.client;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;

import java.util.concurrent.TimeUnit;

/**
 * Collections of helper to work with LxdClient and Rx
 */
public abstract class LxdClientHelpers {

    public static Completable retryOnFailure(Completable t, int numberOfRetry) {
        return t.toObservable().retryWhen(errors -> errors
                    .zipWith(Observable.range(1, numberOfRetry), (n, i) -> i)
                    .flatMap(retryCount -> Observable.timer((long) retryCount, TimeUnit.SECONDS)))
            .ignoreElements();
    }
}
