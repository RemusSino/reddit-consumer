/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer;

import eu.rsino.redditconsumer.consumer.CommentRetriever;
import eu.rsino.redditconsumer.model.Comment;
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
public class CommentRetrieverTest {

    @Autowired
    private CommentRetriever commentRetriever;

    @Test
    public void test() {
        List<Comment> pythonComments = commentRetriever.getComments(Submission.builder()
                .redditId("bk8c2r").subreddit("python").build(), "");
        assertThat(pythonComments).isNotEmpty();
        assertThat(pythonComments.get(0).getRedditId()).isNotEmpty();
        assertThat(pythonComments.get(0).getComment()).isNotEmpty();
        assertThat(pythonComments.get(0).getCreationTimestamp()).isNotZero();
    }
}
