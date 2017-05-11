package com.rxjavatest.networkutils.interfaces;

import com.rxjavatest.models.CommitModel;

import java.util.List;

import retrofit2.Response;

/**
 * Created by rajesh on 29/4/17.
 */

public interface OnRetrofitResult {
    public void onCommitResult(boolean result, Response<List<CommitModel>> code);
}
