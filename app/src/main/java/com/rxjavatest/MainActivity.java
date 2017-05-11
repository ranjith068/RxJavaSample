package com.rxjavatest;

import android.os.SystemClock;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rxjavatest.adapters.ListAdapterHolder;
import com.rxjavatest.interfaces.RecyclersOnItemClickListener;
import com.rxjavatest.models.CommitModel;
import com.rxjavatest.networkutils.RemoteApiCalls;
import com.rxjavatest.utils.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements RecyclersOnItemClickListener {

    final Observable<Integer> serverDownloadObservable = Observable.create(emitter -> {
        SystemClock.sleep(10000); // simulate delay
        emitter.onNext(5);
        emitter.onComplete();
    });
    ConnectionDetector connectionDetector;
    List<CommitModel> commitList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ListAdapterHolder adapter;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.button);
        view.setOnClickListener(v -> {
            v.setEnabled(false); // disables the button until execution has finished
            Disposable subscribe = serverDownloadObservable.
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribeOn(Schedulers.io()).
                    subscribe(integer -> {
                        updateTheUserInterface(integer); // this methods updates the ui
                        v.setEnabled(true); // enables it again
                    });
            disposable.add(subscribe);
        });


        connectionDetector = new ConnectionDetector(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new ListAdapterHolder(this, this, commitList);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);


        if (connectionDetector.isConnectingToInternet()) {


            Observable<List<CommitModel>> api = new RemoteApiCalls.Builder().remoteApiCall(this).commitListCall();

            disposable.add(api.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                            new DisposableObserver<List<CommitModel>>() {

                                @Override
                                public void onComplete() {
//                        Timber.d("Retrofit call 1 completed");
                                }

                                @Override
                                public void onError(Throwable e) {
//                        Timber.e(e, "woops we got an error while getting the list of contributors");
                                }

                                @Override
                                public void onNext(List<CommitModel> contributors) {

                                    commitList.addAll(contributors);
                                    adapter.notifyDataSetChanged();

                                }
                            }));


        } else {
            displayAlert();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void updateTheUserInterface(int integer) {
        TextView view = (TextView) findViewById(R.id.resultView);
        view.setText(String.valueOf(integer));
    }


    @Override
    public void onItemClick(int position, View v) {

    }



}
