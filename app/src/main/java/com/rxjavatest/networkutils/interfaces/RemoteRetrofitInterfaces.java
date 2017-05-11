package com.rxjavatest.networkutils.interfaces;

import com.rxjavatest.models.CommitModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by rajesh on 29/4/17.
 */

public interface RemoteRetrofitInterfaces {

//    @GET("repos/rails/rails/commits")
//    Call<List<CommitModel>> getCommits();

    @GET("repos/rails/rails/commits")
    Observable<List<CommitModel>> getCommits();
}
