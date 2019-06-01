/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer.model;

import java.util.Collections;
import java.util.List;

public class SubmissionListWrapper {
    private List<Submission> submissions;

    public SubmissionListWrapper(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public List<Submission> get() {
        return Collections.unmodifiableList(this.submissions);
    }
}
