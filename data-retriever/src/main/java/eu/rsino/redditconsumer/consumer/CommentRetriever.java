/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.rsino.redditconsumer.model.Comment;
import eu.rsino.redditconsumer.model.CommentListWrapper;
import eu.rsino.redditconsumer.model.Submission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class CommentRetriever {
    private static String url = "https://www.reddit.com/r/%s/comments/%s/.json?limit=100&before=%s";
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public CommentRetriever(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    //todo: reuse the code
    public List<Comment> getComments(Submission submission, String lastCommentFullName) {
        log.info("Retrieving comments for submission: " + submission);
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-agent", "Chrome");
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> commentResponse = restTemplate.exchange(String.format(url, submission.getSubreddit(), submission
                .getRedditId(), lastCommentFullName), HttpMethod.GET, httpEntity, String.class);
        if (!commentResponse.getStatusCode().is2xxSuccessful()
                && commentResponse.hasBody())
            return Collections.emptyList();

        try {
            return objectMapper.readValue(commentResponse.getBody(), CommentListWrapper.class).get();
        } catch (IOException e) {
            log.error("Exception when deserialize submission response " + commentResponse.getBody(), e);
            return Collections.emptyList();
        }
    }
}
