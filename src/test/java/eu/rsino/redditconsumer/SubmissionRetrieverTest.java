/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer;

import eu.rsino.redditconsumer.consumer.SubmissionRetriever;
import eu.rsino.redditconsumer.model.Submission;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SubmissionRetrieverTest {
    @Autowired
    private SubmissionRetriever submissionRetriever;

    @Test
    public void test() {
        List<Submission> pythonSubmission = submissionRetriever.getSubmissions("python");
        assertThat(pythonSubmission).isNotEmpty();
    }
}
