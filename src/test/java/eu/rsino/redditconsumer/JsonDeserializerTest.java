/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.rsino.redditconsumer.model.CommentList;
import eu.rsino.redditconsumer.model.SubmissionList;
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
        SubmissionList submissionList = objectMapper.readValue(resource, SubmissionList.class);
        assertThat(submissionList.get()).hasSize(25);
    }

    @Test
    public void testCommentListDeserializer() throws IOException {
        File resource = new ClassPathResource("reddit-comment.json").getFile();
        CommentList commentList = objectMapper.readValue(resource, CommentList.class);
        assertThat(commentList.get()).isNotEmpty();
    }
}
