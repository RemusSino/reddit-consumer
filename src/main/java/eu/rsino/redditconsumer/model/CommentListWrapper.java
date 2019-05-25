/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer.model;

import java.util.Collections;
import java.util.List;

public class CommentListWrapper {
    private List<Comment> commentList;

    public CommentListWrapper(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<Comment> get() {
        return Collections.unmodifiableList(commentList);
    }
}
