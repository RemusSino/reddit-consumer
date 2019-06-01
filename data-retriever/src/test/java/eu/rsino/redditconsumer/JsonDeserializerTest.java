/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.rsino.redditconsumer.model.CommentListWrapper;
import eu.rsino.redditconsumer.model.SubmissionListWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JsonDeserializerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSubmissionListDeserializer() throws IOException {
        File resource = new ClassPathResource("reddit-submission.json").getFile();
        SubmissionListWrapper submissionList = objectMapper.readValue(resource, SubmissionListWrapper.class);
        assertThat(submissionList.get()).hasSize(25);
    }

    @Test
    public void testCommentListDeserializer() throws IOException {
        File resource = new ClassPathResource("reddit-comment.json").getFile();
        CommentListWrapper commentList = objectMapper.readValue(resource, CommentListWrapper.class);
        assertThat(commentList.get()).isNotEmpty();
    }
}
