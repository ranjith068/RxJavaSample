package com.rxjavatest.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajesh on 29/4/17.
 */

public class CommitModel {

    public String html_url;

    public Author author;

    public List<Parents> parents = new ArrayList<>();

    public Commit commit;

    public String sha;

    public String comments_url;

    public Committer committer;

    public String url;
}
